package cn.azhicloud.olserv.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.Application;
import cn.azhicloud.olserv.repository.AccountRepository;
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
    @Column(nullable = false)
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
     * 订阅信息
     */
    @Transient
    private Subscribe subscribe;

    public static AccountRepository repository() {
        return Application.getBean(AccountRepository.class);
    }

    public static Account of(String id) {
        return repository().findById(id).orElseThrow(() -> new BizException("账户不存在"));
    }
}
