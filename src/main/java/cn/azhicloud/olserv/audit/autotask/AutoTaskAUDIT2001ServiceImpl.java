package cn.azhicloud.olserv.audit.autotask;

import java.util.List;

import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.audit.AuditTaskTypeConst;
import cn.azhicloud.olserv.audit.autotask.bo.TaskAUDIT2001BO;
import cn.azhicloud.olserv.audit.autotask.bo.TaskAUDIT2002BO;
import cn.azhicloud.olserv.audit.domain.entity.AccessKey;
import cn.azhicloud.olserv.audit.domain.entity.AuditLog;
import cn.azhicloud.olserv.audit.repository.AccessKeyRepository;
import cn.azhicloud.olserv.audit.repository.AuditLogRepository;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:12
 */
@Service(AuditTaskTypeConst.PROCESS_AUDIT_MESSAGE)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskAUDIT2001ServiceImpl implements AutoTaskExecuteService {

    private final AccessKeyRepository accessKeyRepository;
    private final AccountRepository accountRepository;
    private final AuditLogRepository auditLogRepository;
    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskAUDIT2001BO taskBO = JSON.parseObject(taskData, TaskAUDIT2001BO.class);

        AccessKey query = new AccessKey();
        query.setPassword(taskBO.getSecret());
        List<AccessKey> keys = accessKeyRepository.findAll(Example.of(query));

        ExecutorHelper.execute(keys, k -> {
            AuditLog auditLog = new AuditLog();
            Account account = accountRepository.findByUsername(k.getName());
            if (account != null) {
                auditLog.setAccountId(account.getId());
            }
            auditLog.setUsername(k.getName());
            auditLog.setServerId(k.getServerId());
            auditLog.setServerName(k.getServerName());
            auditLog.setClientAddr(taskBO.getClientAddr().split(":")[0]);
            auditLog.setTarget(taskBO.getTarget().split(":")[0]);
            // 东八区
            auditLog.setConnectionTime(taskBO.getTime().plusHours(8));
            auditLogRepository.save(auditLog);

            // 发布任务 AUDIT2002 保存客户端IP归属地
            TaskAUDIT2002BO newTaskBO = new TaskAUDIT2002BO();
            newTaskBO.setAuditLogId(auditLog.getId());
            autoTaskBaseService.createAutoTaskAndPublishMQ(
                    AuditTaskTypeConst.SAVE_AUDIT_LOG_CLIENT_LOCATION, JSON.toJSONString(newTaskBO));
        });
    }
}
