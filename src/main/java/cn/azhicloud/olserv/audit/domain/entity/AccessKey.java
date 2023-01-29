package cn.azhicloud.olserv.audit.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:05
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccessKey {

    public static final String FIELD_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 服务器ID
     */
    private String serverId;

    /**
     * 服务器名
     */
    private String serverName;

    /**
     * 单个服务器对应的 access-key ID
     */
    private String keyId;

    /**
     * access-key 名称
     */
    private String name;

    /**
     * access-key 密码
     */
    private String password;

    /**
     * access-key 端口
     */
    private Integer port;

    /**
     * access-key 加密方式
     */
    private String method;

    /**
     * access-key 链接
     */
    private String accessUrl;
}
