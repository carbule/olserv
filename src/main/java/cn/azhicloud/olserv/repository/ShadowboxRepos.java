package cn.azhicloud.olserv.repository;

import java.util.Optional;

import cn.azhicloud.olserv.BaseRepository;
import cn.azhicloud.olserv.model.entity.Shadowbox;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:38
 */
public interface ShadowboxRepos extends BaseRepository<Shadowbox> {

    /**
     * 不允许存在多个 outline 服务器
     * @param apiUrl 接口访问地址
     * @return Optional
     */
    Optional<Shadowbox> findByApiUrl(String apiUrl);
}
