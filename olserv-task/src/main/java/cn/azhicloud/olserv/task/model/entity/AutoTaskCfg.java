package cn.azhicloud.olserv.task.model.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import cn.azhicloud.olserv.task.constant.TaskTypeConst;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/4 15:27
 */
@Entity
@Data
public class AutoTaskCfg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 任务类型 {@link TaskTypeConst}
     */
    @Column(unique = true, nullable = false, length = 10)
    private String taskType;

    /**
     * 任务描述
     */
    @Column(nullable = false, length = 50)
    private String taskDesc;

    /**
     * 任务实现
     */
    @Column(nullable = false, unique = true, length = 50)
    private String execServiceId;

    /**
     * 是否启用该任务
     */
    @Column(columnDefinition = "bit not null default 1")
    private Boolean enabled;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;
}