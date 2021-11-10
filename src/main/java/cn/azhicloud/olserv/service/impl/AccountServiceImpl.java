package cn.azhicloud.olserv.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import cn.azhicloud.idgen.service.IdGenService;
import cn.azhicloud.olserv.model.CreateAccountRequest;
import cn.azhicloud.olserv.model.CreateAccountResponse;
import cn.azhicloud.olserv.model.ListAccessKeysResponse;
import cn.azhicloud.olserv.model.ListAccountsResponse;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccessKeyRepos;
import cn.azhicloud.olserv.repository.AccessLogRepos;
import cn.azhicloud.olserv.repository.AccountRepos;
import cn.azhicloud.olserv.repository.ShadowboxRepos;
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.OutlineManagerService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepos accountRepos;

    private final Hashids hashids;

    private final OutlineManagerService outlineManagerService;

    private final ShadowboxRepos shadowboxRepos;

    private final AccessKeyRepos accessKeyRepos;

    private final AccessLogRepos accessLogRepos;

    private final IdGenService idGenService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateAccountResponse createNew(CreateAccountRequest request) {

        CreateAccountResponse response = new CreateAccountResponse();
        response.setAccounts(new ArrayList<>());

        request.getNames().forEach(name -> {
            Account data = new Account();
            data.setUsername(name);
            Account saved = accountRepos.save(data);

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
        List<Account> all = accountRepos.findAll();

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
    public ListAccessKeysResponse listAccessKeys(String hid) {
        long[] ids = hashids.decode(hid);
        if (ids.length != 1) {
            throw new RuntimeException("hid is not expected");
        }

        Account account = accountRepos.findById(ids[0]).orElseThrow(() ->
                new RuntimeException("account not exist"));

        ListAccessKeysResponse response = new ListAccessKeysResponse();
        response.setAccessKeys(new ArrayList<>());

        List<cn.azhicloud.olserv.model.entity.AccessKey> keys = accessKeyRepos.findByName(account.getUsername());
        keys.forEach(key -> {
            ListAccessKeysResponse.AccessKey keyVO = new ListAccessKeysResponse.AccessKey();
            BeanUtils.copyProperties(key, keyVO);
            keyVO.setName(key.getServerName());
            response.getAccessKeys().add(keyVO);
        });

        // 创建访问日志
        accessLogRepos.newAccessLog(account.getUsername(), JSON.toJSONString(response));
        return response;
    }

    @Override
    public String getAccessKeysUrl(String hid) {
        ListAccessKeysResponse response = listAccessKeys(hid);

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        response.getAccessKeys().forEach(k -> {
            try {
                String accessUrl = k.getAccessUrl();

                if (StringUtils.hasText(k.getRedirectAddress()) && k.getRedirectPort() != null) {
                    accessUrl = accessUrl.substring(0, accessUrl.lastIndexOf("@") + 1) + k.getRedirectAddress() + ":" + k.getRedirectPort();
                }

                String encoding = accessUrl + "#" + URLEncoder.encode(k.getName(), "UTF-8");

                joiner.add(encoding);
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        });

        return Base64Utils.encodeToUrlSafeString(joiner.toString().getBytes(StandardCharsets.UTF_8));
    }
}
