package cn.azhicloud.olserv.service;

import cn.azhicloud.olserv.model.AddShadowboxRequest;
import cn.azhicloud.olserv.model.ListShadowboxesResponse;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:38
 */
public interface ShadowboxService {

    /**
     * 添加 outline 服务器
     *
     * @param request req
     */
    void addShadowbox(AddShadowboxRequest request);

    /**
     * 列举所有 outline 服务器
     *
     * @return ListShadowboxesResponse
     */
    ListShadowboxesResponse listShadowboxes();
}
