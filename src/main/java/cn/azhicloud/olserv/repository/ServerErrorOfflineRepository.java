package cn.azhicloud.olserv.repository;

import java.util.List;

import cn.azhicloud.olserv.domain.entity.ServerErrorOffline;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 15:54
 */
public interface ServerErrorOfflineRepository extends JpaRepository<ServerErrorOffline, Long> {

    /**
     * 查询服务器的离线记录
     */
    List<ServerErrorOffline> findByServerIdOrderByCreatedAtDesc(String serverId);
}
