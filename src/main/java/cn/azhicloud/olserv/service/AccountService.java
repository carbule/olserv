package cn.azhicloud.olserv.service;

import java.util.List;

import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:36
 */
public interface AccountService {

    Account createAccount(String username);

    List<Account> listAccounts();

    List<Shadowbox> listShadowboxOwnedByAccount(String id);

    String getAccessKeysSubscribe(String id);
}
