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
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.ShadowboxService;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ShadowboxService shadowboxService;

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
        Account saved = accountRepository.save(account);

        // 为新创建的用户分配 key
        shadowboxService.createAccessKeyForAllShadowbox(saved.getUsername());
        return saved;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll();
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
