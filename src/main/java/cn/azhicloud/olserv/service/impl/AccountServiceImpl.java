package cn.azhicloud.olserv.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import cn.azhicloud.olserv.BaseEntity;
import cn.azhicloud.olserv.model.CreateAccountRequest;
import cn.azhicloud.olserv.model.CreateAccountResponse;
import cn.azhicloud.olserv.model.ListAccountsResponse;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.ShadowboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
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

    private final Hashids hashids;

    private final ShadowboxRepository shadowboxRepos;

    private final ShadowboxService shadowboxService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateAccountResponse createNew(CreateAccountRequest request) {

        CreateAccountResponse response = new CreateAccountResponse();
        response.setAccounts(new ArrayList<>());

        request.getNames().forEach(name -> {
            Account data = BaseEntity.instance(Account.class);
            data.setUsername(name);
            Account saved = accountRepository.save(data);

            CreateAccountResponse.Account vo = new CreateAccountResponse.Account();
            vo.setUid(hashids.encode(saved.getId()));
            vo.setUsername(saved.getUsername());
            vo.setCreated(saved.getCreated());

            response.getAccounts().add(vo);
        });

        return response;
    }

    @Override
    public ListAccountsResponse listAccounts() {
        List<Account> all = accountRepository.findAll();

        ListAccountsResponse response = new ListAccountsResponse();
        response.setAccounts(new ArrayList<>());

        all.forEach(o -> {
            ListAccountsResponse.Account vo = new ListAccountsResponse.Account();
            vo.setHid(hashids.encode(o.getId()));
            vo.setUsername(o.getUsername());
            vo.setCreated(o.getCreated());

            response.getAccounts().add(vo);
        });
        return response;
    }

    @Override
    @Transactional
    public List<Shadowbox> listShadowboxOwnedByAccount(String hid) {
        long[] ids = hashids.decode(hid);
        if (ids.length != 1) {
            throw new RuntimeException("hid is not expected");
        }

        Account account = accountRepository.findById(ids[0]).orElseThrow(() ->
                new RuntimeException("account not exist"));
        account.setLastAccess(new Date());

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
    public String getAccessKeysUrl(String hid) {
        List<Shadowbox> boxes = listShadowboxOwnedByAccount(hid);

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
