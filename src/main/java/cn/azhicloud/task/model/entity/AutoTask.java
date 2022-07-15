package cn.azhicloud.task.model.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/4 15:27
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AutoTask {

    /**
     * 失败原因字段最大长度
     */
    public static final int FIELD_FAILED_REASON_LENGTH = 2000;

    /**
     * 任务 ID
     */
    @Id
    @GeneratedValue
    private Long taskId;

    /**
     * 任务编号
     */
    @Column(nullable = false, length = 36)
    private String taskNo;

    /**
     * 任务类型
     */
    @Column(nullable = false, length = 10)
    private String taskType;

    /**
     * 任务所需业务数据
     */
    @Column(length = 1000)
    private String taskData;

    /**
     * 任务状态
     */
    @Column(nullable = false, length = 2)
    private String status;

    /**
     * 失败原因
     */
    @Column(length = FIELD_FAILED_REASON_LENGTH)
    private String failedReason;

    /**
     * 执行人
     */
    private String executor;

    /**
     * 执行开始时间
     */
    private LocalDateTime executeStartAt;

    /**
     * 执行结束时间
     */
    private LocalDateTime executeEndAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
