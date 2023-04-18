package cn.azhicloud.olserv.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.base.model.entity.ControlParameter;
import cn.azhicloud.infra.base.repository.ControlParameterRepository;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1001BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2004BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK3001BO;
import cn.azhicloud.olserv.constant.ControlParameters;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.constant.em.NotifyStatusEnum;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.PullHistory;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.domain.entity.Subscribe;
import cn.azhicloud.olserv.domain.model.CreateAccountRQ;
import cn.azhicloud.olserv.domain.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.*;
import cn.azhicloud.olserv.service.AccountService;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;


/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    public static final String REQUEST_HEADER_FROM_IP = "fromIP";

    private final AccountRepository accountRepository;

    private final OutlineRepository outlineRepository;

    private final AutoTaskBaseService autoTaskBaseService;

    private final HttpServletRequest httpServletRequest;

    private final SubscribeRepository subscribeRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final PullHistoryRepository pullHistoryRepository;

    private final ControlParameterRepository controlParameterRepository;

    @Override
    @Transactional
    public Account createAccount(CreateAccountRQ rq) {
        Account existed = accountRepository.findByUsernameOrEmail(rq.getUsername(), rq.getEmail());
        if (existed != null) {
            throw new BizException("账户名或者邮箱重复");
        }

        Account account = new Account();
        account.setId(NanoIdUtils.randomNanoId());
        LocalDateTime now = LocalDateTime.now();
        account.setCreatedAt(now);
        account.setExpiredAt(now.plusDays(30L));
        account.setUsername(rq.getUsername());
        account.setEnableNotice(Boolean.TRUE);
        account.setEmail(rq.getEmail());
        account.setMegabytesTransferred(0L);
        // 默认分配 200G 流量
        account.setMegabytesAllocate(200 * 1024L);
        accountRepository.save(account);

        // 生成订阅和订阅短链接
        generateSubscribeAndShortLink(account);

        // 为新创建的用户分配 key
        TaskTASK1001BO taskBO = new TaskTASK1001BO();
        taskBO.setAccountId(account.getId());
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.ALLOCATE_ACCOUNT_TO_SHADOWBOXES,
                JSON.toJSONString(taskBO));
        return account;
    }

    protected void generateSubscribeAndShortLink(Account account) {
        ControlParameter controlParameter = controlParameterRepository.findByParamCodeAndEnabledTrue(ControlParameters.SUBSCRIBE_URL_TEMPLATE);
        if (controlParameter == null) {
            throw new BizException("未配置订阅链接模版");
        }
        ControlParameter controlParameter1 = controlParameterRepository.findByParamCodeAndEnabledTrue(ControlParameters.SSCONF_URL_TEMPLATE);
        if (controlParameter1 == null) {
            throw new BizException("未配置 outline-client 动态密钥模版");
        }

        String subscribeLink = MessageFormat.format(controlParameter.getParamValue(), account.getId());
        Subscribe sub = new Subscribe();
        sub.setCreatedAt(LocalDateTime.now());
        sub.setAccountId(account.getId());
        sub.setSubscribeLink(subscribeLink);
        sub.setSsconfLink(MessageFormat.format(controlParameter1.getParamValue(), account.getId()));
        subscribeRepository.save(sub);

        account.setSubscribe(sub);
    }


    @Override
    public List<Account> listAccounts() {
        List<Account> all = accountRepository.findAll();

        Map<String, Subscribe> subscribeMap = subscribeRepository.findByAccountIdIn(
                        all.stream().map(Account::getId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Subscribe::getAccountId, Function.identity()));

        for (Account account : all) {
            Subscribe sub = subscribeMap.get(account.getId());
            if (sub == null) {
                {
                    // 生成自动任务 TASK3001 生成订阅链接和短链接
                    TaskTASK3001BO taskBO = new TaskTASK3001BO();
                    taskBO.setAccountId(account.getId());
                    autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.GENERATE_SUBSCRIBE_AND_SHORT_URL,
                            JSON.toJSONString(taskBO));
                }
                continue;
            }
            account.setSubscribe(sub);
        }
        return all;
    }

    @Override
    @Transactional
    public List<Shadowbox> listShadowboxOwnedByAccount(String id) {
        Account account = Account.of(id);
        // 记录账户拉取订阅的时间
        account.setLastAccess(LocalDateTime.now());

        // 记录拉取历史
        PullHistory history = new PullHistory();
        history.setAccountId(account.getId());
        history.setAccountUsername(account.getUsername());
        history.setFromIp(getFromIP());
        history.setNotifyStatus(NotifyStatusEnum.READY.name());
        pullHistoryRepository.save(history);
        // 发布自动任务 TASK2004，记录订阅历史中的地理位置
        autoTaskTASK2004(history.getId());

        List<Shadowbox> boxes = shadowboxRepository.findByOfflineIsFalse();
        ExecutorHelper.execute(boxes, box -> {
            URI uri = URI.create(box.getApiUrl());
            // 如果服务端有变更，托管态实体自动更新
            BeanUtils.copyProperties(outlineRepository.returnsInformationAboutTheServer(uri), box);
            box.setAccessKey(outlineRepository.getAccessKey(uri, account.getUsername()));
        }, ex -> log.error("获取 Key 失败：{}", ex.getMessage()));

        // 剔除未分配 Key 的服务器
        boxes = boxes.stream()
                .filter(shadowbox -> shadowbox.getAccessKeys().size() > 0)
                .sorted(Comparator.comparing(Shadowbox::getName))
                .collect(Collectors.toList());
        // 实体托管态更新
        history.setPullContent(JSON.toJSONString(boxes));
        return boxes;
    }

    private String getFromIP() {
        String fromIP = httpServletRequest.getHeader(REQUEST_HEADER_FROM_IP);
        if (StringUtils.isBlank(fromIP)) {
            fromIP = ServletUtil.getClientIP(httpServletRequest);
        }
        return fromIP;
    }

    protected void autoTaskTASK2004(Long historyId) {
        TaskTASK2004BO taskBO = new TaskTASK2004BO();
        taskBO.setHistoryId(historyId);
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.SAVE_PULL_HISTORY_LOCATION,
                JSON.toJSONString(taskBO));
    }


    @Override
    @Transactional
    public String getAccessKeysSubscribe(String id) {
        List<Shadowbox> boxes = listShadowboxOwnedByAccount(id);

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        boxes.forEach(box -> {
            try {
                AccessKey key = box.getAccessKeys().get(0);
                String encoding = key.getAccessUrl() + "#" + URLEncoder.encode(box.getName(), "UTF-8");
                joiner.add(encoding);
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        });

        return Base64Utils.encodeToUrlSafeString(joiner.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    @Transactional
    public void trafficReset(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw BizException.format("账户 %s 不存在", username);
        }

        if ((account.getMegabytesAllocate() - account.getMegabytesTransferred()) > 1024) {
            throw new BizException("只有剩余流量小于 1G 才可以重置");
        }
        account.setMegabytesTransferred(0L);

        // 发布自动任务，为账户重新分配节点
        TaskTASK1001BO taskBO = new TaskTASK1001BO();
        taskBO.setAccountId(account.getId());
        taskBO.setResetTraffic(true);
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.ALLOCATE_ACCOUNT_TO_SHADOWBOXES,
                JSON.toJSONString(taskBO));
    }

    @Override
    @Transactional
    public String getAccessKeyForOutlineClientUseSSConf(String accountId) {
        List<Shadowbox> shadowboxes = listShadowboxOwnedByAccount(accountId);
        Optional<Shadowbox> any = shadowboxes.stream().filter(e -> Boolean.TRUE.equals(e.getForOutlineClient()))
                .findAny();
        if (!any.isPresent()) {
            throw new BizException("未配置为 outline-client 提供动态链接的服务器");
        }

        List<AccessKey> keys = any.get().getAccessKeys();
        if (keys.isEmpty())
            return "";
        ControlParameter saltPrefix = controlParameterRepository.findByParamCodeAndEnabledTrue(ControlParameters.SS_SALT_PREFIX);
        return keys.get(0).saltEncrypt(saltPrefix == null ? null : saltPrefix.getParamValue());
    }

}
