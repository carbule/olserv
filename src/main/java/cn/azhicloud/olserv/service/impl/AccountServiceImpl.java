package cn.azhicloud.olserv.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1001BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2002BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2003BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK3001BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.model.CreateAccountRQ;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.entity.Subscribe;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.repository.SubscribeRepository;
import cn.azhicloud.olserv.service.AccountService;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${template.subscribe-link}")
    private String accountSubscribeUrlTemplate;

    private final SubscribeRepository subscribeRepository;

    private final ShadowboxRepository shadowboxRepository;

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
        String subscribeLink = MessageFormat.format(accountSubscribeUrlTemplate, account.getId());
        Subscribe sub = new Subscribe();
        sub.setCreatedAt(LocalDateTime.now());
        sub.setAccountId(account.getId());
        sub.setSubscribeLink(subscribeLink);
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

        // 记录账户拉取订阅的时间和访问 IP（可能通过 FZero 服务访问）
        account.setLastAccess(LocalDateTime.now());
        String fromIP = httpServletRequest.getHeader(REQUEST_HEADER_FROM_IP);
        if (StringUtils.isBlank(fromIP)) {
            account.setFromIp(ServletUtil.getClientIP(httpServletRequest));
        } else {
            account.setFromIp(fromIP);
        }
        // 发布自动任务 TASK2003 获取账户拉取订阅时的地理位置
        autoTaskTASK2003(account.getId());

        List<Shadowbox> boxes = shadowboxRepository.findAll();
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

        // 发送通知邮件
        autoTaskTASK2002(account, boxes);
        return boxes;
    }

    protected void autoTaskTASK2003(String accountId) {
        TaskTASK2003BO taskBO = new TaskTASK2003BO();
        taskBO.setAccountId(accountId);
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.SAVE_ACCOUNT_PULL_SUBSCRIBE_LOCATION,
                JSON.toJSONString(taskBO));
    }

    protected void autoTaskTASK2002(Account account, List<Shadowbox> boxes) {
        TaskTASK2002BO taskBO = new TaskTASK2002BO();
        taskBO.setAccountId(account.getId());
        taskBO.setNodes(boxes.stream().map(Shadowbox::getName).collect(Collectors.toList()));
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.ACCOUNT_PULL_SUBSCRIBE_NOTICE,
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

}
