package cn.azhicloud.fzero.model.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 00:18
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ShortLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 唯一编码
     */
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    /**
     * 链接地址
     */
    @Column(nullable = false)
    private String link;
}
