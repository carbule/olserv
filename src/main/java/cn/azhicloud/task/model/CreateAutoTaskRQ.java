package cn.azhicloud.task.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 19:29
 */
@Data
public class CreateAutoTaskRQ {

    @NotBlank(message = "taskType 不能为空")
    private String taskType;

    private String taskData;
}
