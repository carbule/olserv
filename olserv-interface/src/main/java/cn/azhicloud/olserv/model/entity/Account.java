package cn.azhicloud.olserv.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:33
 */
@Data
@Entity
public class Account {

    @Id
    private String id;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 过期时间
     */
    private LocalDateTime expiredAt;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * 是否启用邮件通知
     */
    @Column(columnDefinition = "bit default 1")
    private Boolean enableNotice;

    /**
     * 最后拉取订阅时间
     */
    private LocalDateTime lastAccess;

    /**
     * 已使用的兆字节数
     */
    private Long megabytesTransferred;

    /**
     * 分配的兆字节流量
     */
    private Long megabytesAllocate;
    /**
     * 访问者 IP
     */
    @Column(length = 16)
    private String fromIp;

    /**
     * 访问者地理位置
     */
    @Column(length = 100)
    private String fromLocation;

    /**
     * 订阅地址
     */
    @Transient
    private String subscribe;
}
