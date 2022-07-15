package cn.azhicloud.task.controller;

import cn.azhicloud.task.model.CreateAutoTaskRQ;
import cn.azhicloud.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 19:28
 */
@RestController
@RequestMapping("/test/auto-task")
@RequiredArgsConstructor
public class TestAutoTaskController {

    private final AutoTaskBaseService autoTaskBaseService;

    @PostMapping("/create")
    public void createAutoTask(@RequestBody @Validated CreateAutoTaskRQ rq) {
        autoTaskBaseService.createAutoTaskAndPublicMQ(rq.getTaskType(), rq.getTaskData());
    }
}
