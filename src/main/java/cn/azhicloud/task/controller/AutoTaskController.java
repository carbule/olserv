package cn.azhicloud.task.controller;

import cn.azhicloud.task.constant.ActiveMQQueueConst;
import cn.azhicloud.task.constant.Executor;
import cn.azhicloud.task.service.AutoTaskBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 18:48
 */
@RestController
@RequestMapping("/auto-task")
@RequiredArgsConstructor
public class AutoTaskController {

    private final AutoTaskBizService autoTaskBizService;

    /**
     * 任务执行调度入口
     */
    @GetMapping("/job/execute")
    public void executeByJob() {
        autoTaskBizService.executeByJob();
    }

    /**
     * 任务执行消息队列入口
     */
    @JmsListener(destination = ActiveMQQueueConst.QUEUE_AUTO_TASK)
    public void executeByMQ(String message) {
        autoTaskBizService.execute(message, Executor.MQ.name());
    }
}
