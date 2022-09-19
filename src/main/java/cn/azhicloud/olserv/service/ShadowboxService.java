package cn.azhicloud.olserv.service;

import java.util.List;

import cn.azhicloud.olserv.model.entity.Shadowbox;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:38
 */
public interface ShadowboxService {

    /**
     * 新增新的服务器
     *
     * @param apiUrl 服务器管理 API
     * @return 服务器信息
     */
    Shadowbox addShadowbox(String apiUrl);

    /**
     * 获取所有服务器信息，包括其拥有的 Keys
     *
     * @return 服务器列表
     */
    List<Shadowbox> listShadowboxes();
}
