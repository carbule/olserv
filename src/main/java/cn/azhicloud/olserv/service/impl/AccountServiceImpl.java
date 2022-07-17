package cn.azhicloud.olserv.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.ShadowboxService;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskTASK1001BO;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskBaseService;
import com.alibaba.fastjson.JSON;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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

    private final AccountRepository accountRepository;

    @Autowired
    @Lazy
    private ShadowboxService shadowboxService;

    private final OutlineRepository outlineRepository;

    private final AutoTaskBaseService autoTaskBaseService;

    @Value("${url-template.account-subscribe}")
    private String accountSubscribeUrlTemplate;

    @Override
    @Transactional
    public Account createAccount(String username) {
        Account existed = accountRepository.findByUsername(username);
        if (existed != null) {
            throw new RuntimeException("repeat");
        }

        Account account = new Account();
        account.setId(NanoIdUtils.randomNanoId());
        LocalDateTime now = LocalDateTime.now();
        account.setCreatedAt(now);
        account.setExpiredAt(now.plusDays(30L));
        account.setUsername(username);
        account.setMegabytesTransferred(0L);
        // 默认分配 200G 流量
        account.setMegabytesAllocate(200 * 1024L);
        accountRepository.save(account);

        account.setSubscribe(accountSubscribeUrlTemplate.replace("{accountId}", account.getId()));

        // 为新创建的用户分配 key
        TaskTASK1001BO taskBO = new TaskTASK1001BO();
        taskBO.setAccountId(account.getId());
        autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.ALLOCATE_ACCOUNT_TO_SHADOWBOXES,
                JSON.toJSONString(taskBO));
        return account;
    }

    @Override
    public List<Account> listAccounts() {
        List<Account> all = accountRepository.findAll();
        all.forEach(account -> account.setSubscribe(accountSubscribeUrlTemplate
                .replace("{accountId}", account.getId())));
        return all;
    }

    @Override
    @Transactional
    public List<Shadowbox> listShadowboxOwnedByAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new RuntimeException("account not exist"));
        account.setLastAccess(LocalDateTime.now());

        List<Shadowbox> boxes = shadowboxService.listShadowboxes();

        for (Iterator<Shadowbox> it = boxes.iterator(); it.hasNext(); ) {
            Shadowbox box = it.next();
            Optional<AccessKey> first = box.getAccessKeys().stream()
                    .filter(k -> Objects.equals(account.getUsername(), k.getName()))
                    .findFirst();

            if (first.isPresent()) {
                box.setAccessKeys(Collections.singletonList(first.get()));
            } else {
                it.remove();
            }
        }
        boxes.sort(Comparator.comparing(Shadowbox::getName));
        return boxes;
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

}
