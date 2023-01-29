package cn.azhicloud.olserv.audit.repository;

import cn.azhicloud.olserv.audit.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/29 10:44
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
