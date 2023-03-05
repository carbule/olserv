package cn.azhicloud.olserv.service;

import java.util.List;

import cn.azhicloud.olserv.domain.model.CreateAccountRQ;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.Shadowbox;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:36
 */
public interface AccountService {

    /**
     * 创建一个新账户
     *
     * @param rq 参数
     * @return account info
     */
    Account createAccount(CreateAccountRQ rq);

    /**
     * 获取所有账户
     *
     * @return account list
     */
    List<Account> listAccounts();

    /**
     * 获取账户拥有的所有服务器信息
     *
     * @param id accountId
     * @return box list
     */
    List<Shadowbox> listShadowboxOwnedByAccount(String id);

    /**
     * 获取账户订阅字符串
     *
     * @param id accountId
     * @return subscribe
     */
    String getAccessKeysSubscribe(String id);

    /**
     * 重置用户的流量
     *
     * @param username 用户名
     */
    void trafficReset(String username);

    /**
     * 提供 outline-client 需要的动态密钥
     *
     * @param accountId 账户 ID
     * @return 密钥
     */
    String getAccessKeyForOutlineClientUseSSConf(String accountId);
}
