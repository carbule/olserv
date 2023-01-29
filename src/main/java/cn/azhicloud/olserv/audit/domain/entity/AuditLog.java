package cn.azhicloud.olserv.audit.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.Application;
import cn.azhicloud.olserv.audit.repository.AuditLogRepository;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/29 10:36
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 服务器ID
     */
    private String serverId;

    /**
     * 服务器名
     */
    private String serverName;

    /**
     * 客户端IP地址
     */
    private String clientAddr;

    /**
     * 客户端IP归属地
     */
    private String clientLocation;

    /**
     * 访问目标地址
     */
    private String target;

    /**
     * 连接时间
     */
    private LocalDateTime connectionTime;

    public static AuditLogRepository repository() {
        return Application.getBean(AuditLogRepository.class);
    }

    public static AuditLog of(Long id) {
        return repository().findById(id).orElseThrow(() ->
                BizException.format("%s(%s) 实体不存在", AuditLog.class.getSimpleName(), id));
    }
}
