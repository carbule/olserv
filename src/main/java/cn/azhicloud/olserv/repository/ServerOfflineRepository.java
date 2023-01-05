package cn.azhicloud.olserv.repository;

import java.util.List;

import cn.azhicloud.olserv.domain.entity.ServerOffline;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 15:54
 */
public interface ServerOfflineRepository extends JpaRepository<ServerOffline, Long> {

    /**
     * 查询服务器的离线记录
     */
    List<ServerOffline> findByServerIdOrderByCreatedAtDesc(String serverId);
}
