package cn.azhicloud.olserv.task.service.impl;

import cn.azhicloud.olserv.infra.BizType;
import cn.azhicloud.olserv.infra.helper.SystemHelper;
import cn.azhicloud.olserv.task.constant.ActiveMQQueueConst;
import cn.azhicloud.olserv.task.constant.TaskStatus;
import cn.azhicloud.olserv.task.model.entity.AutoTask;
import cn.azhicloud.olserv.task.repository.AutoTaskRepository;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import com.xiaoju.uemc.tinyid.client.utils.TinyId;
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
        task.setTaskId(TinyId.nextId(BizType.APPLICATION));
        task.setTaskNo(SystemHelper.nextSerialNo());
        task.setTaskType(taskType);
        task.setTaskData(taskData);
        task.setStatus(TaskStatus.PENDING.value);

        autoTaskRepository.save(task);
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
}
