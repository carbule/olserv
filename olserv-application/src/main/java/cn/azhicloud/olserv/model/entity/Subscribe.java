package cn.azhicloud.olserv.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 22:27
 */
@Data
@Entity
public class Subscribe {

    @Id
    private Long id;

    private LocalDateTime createdAt;

    /**
     * 账户 ID
     */
    @Column(nullable = false, unique = true)
    private String accountId;

    /**
     * 订阅链接
     */
    @Column(nullable = false, unique = true)
    private String subscribeLink;

    /**
     * 订阅短链接
     */
    @Column(nullable = false, unique = true)
    private String shortLink;
}
