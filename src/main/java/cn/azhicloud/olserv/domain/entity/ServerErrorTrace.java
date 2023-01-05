package cn.azhicloud.olserv.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 13:49
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ServerErrorTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 服务器唯一标识 {@link Shadowbox#getApiUrl()}
     */
    private String serverId;

    /**
     * 错误信息
     */
    @Column(columnDefinition = "text")
    private String error;
}

