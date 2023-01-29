package cn.azhicloud.olserv.audit;

import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:45
 */
@Component
@RequiredArgsConstructor
public class AuditMessageConsumer {

    private final AutoTaskBaseService autoTaskBaseService;

//    @JmsListener(destination = "QUEUE.OUTLINE-SS-SERVER.TCP.CONNECTION", concurrency = "10")
//    @Transactional
//    public void process(String message) {
//        autoTaskBaseService.createAutoTaskAndPublishMQ(
//                AuditTaskTypeConst.PROCESS_AUDIT_MESSAGE, message);
//    }

    @JmsListener(destination = "QUEUE.OUTLINE-SS-SERVER.TCP.CONNECTION", concurrency = "10")
    @Transactional
    public void process(ActiveMQBytesMessage message) {
        autoTaskBaseService.createAutoTaskAndPublishMQ(
                AuditTaskTypeConst.PROCESS_AUDIT_MESSAGE, new String(message.getContent().getData()));
    }
}
