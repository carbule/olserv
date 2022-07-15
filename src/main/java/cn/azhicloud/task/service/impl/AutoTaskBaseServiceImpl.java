package cn.azhicloud.task.service.impl;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import cn.azhicloud.task.constant.ActiveMQQueueConst;
import cn.azhicloud.task.constant.TaskStatus;
import cn.azhicloud.task.model.entity.AutoTask;
import cn.azhicloud.task.repository.AutoTaskRepository;
import cn.azhicloud.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 20:05
 */
@Service
@RequiredArgsConstructor
public class AutoTaskBaseServiceImpl implements AutoTaskBaseService {

    private final AutoTaskRepository autoTaskRepository;

    private final JmsMessagingTemplate jmsMessagingTemplate;

    @Override
    @Transactional
    public String createAutoTask(String taskType, String taskData) {
        AutoTask task = new AutoTask();
        task.setTaskNo("");
        task.setTaskType(taskType);
        task.setTaskData(taskData);
        task.setStatus(TaskStatus.PENDING.value);

        autoTaskRepository.save(task);
        task.setTaskNo(getTaskNo(task.getTaskId()));
        return task.getTaskNo();
    }

    @Override
    @Transactional
    public String createAutoTaskAndPublicMQ(String taskType, String taskData) {
        String taskNo = createAutoTask(taskType, taskData);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(
                        ActiveMQQueueConst.QUEUE_AUTO_TASK), taskNo);
            }
        });
        return taskNo;
    }

    private String getTaskNo() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private String getTaskNo(Long id) {
        return FORMATTER.format(LocalDateTime.now()) +
                new DecimalFormat("0000").format(id % 10000);
    }

}
