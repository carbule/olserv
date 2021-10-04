package cn.azhicloud.olserv.service;

import cn.azhicloud.olserv.model.CreateAccountRequest;
import cn.azhicloud.olserv.model.CreateAccountResponse;
import cn.azhicloud.olserv.model.ListAccessKeysResponse;
import cn.azhicloud.olserv.model.ListAccountsResponse;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:36
 */
public interface AccountService {

    /**
     * 新创建新账户
     *
     * @param request req
     * @return CreateAccountResponse
     */
    CreateAccountResponse createNew(CreateAccountRequest request);

    /**
     * 列举所有账号
     *
     * @return ListAccountsResponse
     */
    ListAccountsResponse listAccounts();

    /**
     * 获取归属账户的 access-keys
     *
     * @param hid hash id
     * @return ListAccessKeysResponse
     */
    ListAccessKeysResponse listAccessKeys(String hid);

    /**
     * 获取 access-keys 链接
     *
     * @param hid hash id
     * @return url
     */
    String getAccessKeysUrl(String hid);
}
