package cn.azhicloud.olserv.task.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/20 13:24
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TodoTask {

    @Id
    private Long taskNo;

    private String taskType;

    private LocalDateTime todoTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
