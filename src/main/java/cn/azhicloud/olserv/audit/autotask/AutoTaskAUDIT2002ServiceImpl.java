package cn.azhicloud.olserv.audit.autotask;

import cn.azhicloud.infra.base.model.IPUserAgentInfoResponse;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.audit.AuditTaskTypeConst;
import cn.azhicloud.olserv.audit.autotask.bo.TaskAUDIT2002BO;
import cn.azhicloud.olserv.audit.domain.entity.AuditLog;
import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/29 10:48
 */
@Service(AuditTaskTypeConst.SAVE_AUDIT_LOG_CLIENT_LOCATION)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskAUDIT2002ServiceImpl implements AutoTaskExecuteService {

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskAUDIT2002BO taskBO = JSON.parseObject(taskData, TaskAUDIT2002BO.class);

        AuditLog auditLog = AuditLog.of(taskBO.getAuditLogId());
        // 如果是内网 IP，无法获取地理位置
        if (!NetUtil.isInnerIP(auditLog.getClientAddr())) {
            auditLog.setClientLocation(IPUserAgentInfoResponse.of(auditLog.getClientAddr())
                    .locationString());
        }
    }
}
