package cn.azhicloud.task.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import cn.azhicloud.task.constant.TaskTypeConst;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/4 15:27
 */
@Entity
@Data
public class AutoTaskCfg {

    /**
     * 任务类型 {@link TaskTypeConst}
     */
    @Id
    @Column(length = 10)
    private String taskType;

    /**
     * 任务描述
     */
    @Column(nullable = false, length = 50)
    private String taskDesc;

    /**
     * 任务实现
     */
    @Column(nullable = false, length = 50)
    private String execServiceId;
}
