package cn.azhicloud.olserv.autotask.noticetask;

import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskANOTICE1001BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.ServerOffline;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.helper.MailHelperExtra;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/22 09:49
 */
@Service(TaskTypeConst.A_NOTICE_SERVER_PASSIVE_OFFLINE)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskANOTICE1001ServiceImpl implements AutoTaskExecuteService {

    @Value("${alarm.mail.receiver}")
    private String alarmMailReceiver;

    private final MailHelperExtra mailHelperExtra;

    @Override
    public void execute(String taskData) {
        TaskANOTICE1001BO taskBO = JSON.parseObject(taskData, TaskANOTICE1001BO.class);

        ServerOffline offline = ServerOffline.of(taskBO.getOfflineId());
        Shadowbox shadowbox = Shadowbox.of(offline.getServerId());

        String emailTemplate = "服务器 %s 被动下线，下线时间 %s %s";
        mailHelperExtra.sendSubscribeNotice(alarmMailReceiver, "SERVER PASSIVE OFFLINE",
                String.format(emailTemplate,
                        shadowbox.getName(),
                        offline.getOfflineDurations(),
                        offline.getDurationTimeunit()));
    }
}
