package cn.azhicloud.olserv.service;

import java.util.List;

import cn.azhicloud.olserv.model.entity.Shadowbox;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:38
 */
public interface ShadowboxService {

    Shadowbox addShadowbox(String apiUrl);

    List<Shadowbox> listShadowboxes();

    void createAccessKeyForAllShadowbox(String keyName);
}
