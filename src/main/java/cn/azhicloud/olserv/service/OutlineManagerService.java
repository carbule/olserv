package cn.azhicloud.olserv.service;

import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.ServerInformation;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:49
 */
public interface OutlineManagerService {

    /**
     * 获取服务器信息
     *
     * @param apiUrl 接口访问地址
     * @return ServerInformation
     */
    ServerInformation getServerInformation(String apiUrl);

    /**
     * 获取服务器信息(记录失败原因)
     *
     * @param box shadowbox
     * @return ServerInformation
     */
    ServerInformation getServerInformation(Shadowbox box);

    /**
     * 列举所有的 access-key
     *
     * @param apiUrl 接口访问地址
     * @return AccessKeys
     */
    AccessKeys listAccessKeys(String apiUrl);

    /**
     * 列举所有的 access-key(记录失败原因)
     *
     * @param box shadowbox
     * @return AccessKeys
     */
    AccessKeys listAccessKeys(Shadowbox box);

    /**
     * 获取 access-key
     *
     * @param apiUrl   接口访问地址
     * @param username 用户名
     * @return AccessKey
     */
    AccessKey getAccessKey(String apiUrl, String username);
}
