package cn.azhicloud.olserv.repository;

import java.time.LocalDateTime;

import cn.azhicloud.olserv.domain.entity.ServerErrorTrace;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 14:25
 */
public interface ServerErrorTraceRepository extends JpaRepository<ServerErrorTrace, Long> {

    /**
     * 查询服务器在给定时间之后创建的异常信息
     */
    long countByServerIdAndCreatedAtAfter(String serverId, LocalDateTime createdAt);
}
