package cn.azhicloud.olserv.repository;

import java.util.List;

import cn.azhicloud.olserv.domain.entity.Shadowbox;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:38
 */
public interface ShadowboxRepository extends JpaRepository<Shadowbox, String> {

    /**
     * 查询在线的服务器
     */
    List<Shadowbox> findByOfflineIsFalse();
}
