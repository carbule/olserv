package cn.azhicloud.olserv.autotask;

import java.time.LocalDateTime;
import java.util.List;

import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.infra.task.service.TodoTaskBaseService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1004BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1005BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.ServerErrorOffline;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.helper.OfflineDuration;
import cn.azhicloud.olserv.repository.ServerErrorOfflineRepository;
import cn.azhicloud.olserv.repository.ServerErrorTraceRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 15:13
 */
@Service(TaskTypeConst.SHADOWBOX_OFFLINE_STRATEGY)
@RequiredArgsConstructor
public class AutoTaskTASK1005ServiceImpl implements AutoTaskExecuteService {

    private final ShadowboxRepository shadowboxRepository;
    private final ServerErrorTraceRepository serverErrorTraceRepository;
    private final ServerErrorOfflineRepository serverErrorOfflineRepository;
    private final TodoTaskBaseService todoTaskBaseService;

    /*
    离线时间轮：5分钟、10分钟、30分钟、1小时、2小时、5小时、10小时、1天、2天、永久
     */

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK1005BO taskBO = JSON.parseObject(taskData, TaskTASK1005BO.class);

        Shadowbox box = Shadowbox.of(taskBO.getServerId());

        LocalDateTime now = LocalDateTime.now();
        // 如果一小时内错误超过5条，触发离线
        if (serverErrorTraceRepository.countByServerIdAndCreatedAtAfter(
                box.getServerId(), now.minusHours(1)) > 5) {
            // 托管态自动更新
            box.setOffline(Boolean.TRUE);

            // 获取服务器的离线记录
            List<ServerErrorOffline> offlineList = serverErrorOfflineRepository
                    .findByServerIdOrderByCreatedAtDesc(box.getApiUrl());

            OfflineDuration duration;
            if (CollectionUtils.isNotEmpty(offlineList)) {
                // 根据最新一条离线记录计算时间轮位置
                duration = new OfflineDuration(
                        offlineList.get(0).getOfflineDurations(),
                        offlineList.get(0).getDurationTimeunit());
            } else {
                duration = new OfflineDuration();
            }

            // 保存离线记录
            ServerErrorOffline offline = new ServerErrorOffline();
            offline.setServerId(box.getApiUrl());
            offline.setOfflineDurations(duration.getDurations());
            offline.setDurationTimeunit(duration.getUnit().name());
            serverErrorOfflineRepository.save(offline);

            // 发布待办任务修改服务器为在线状态
            TaskTASK1004BO newTaskBO = new TaskTASK1004BO();
            newTaskBO.setServerId(box.getApiUrl());
            newTaskBO.setOffline(Boolean.FALSE);
            todoTaskBaseService.createTodoTaskAndPublishMQ(
                    TaskTypeConst.UPDATE_SHADOWBOX_OFFLINE_STATUS,
                    JSON.toJSONString(newTaskBO),
                    duration.getDurations(),
                    duration.getUnit());
        }
    }
}


