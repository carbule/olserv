package cn.azhicloud.olserv.autotask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.azhicloud.infra.base.helper.TimeWheelHelper;
import cn.azhicloud.infra.base.model.entity.TimeWheel;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.infra.task.service.TodoTaskBaseService;
import cn.azhicloud.olserv.autotask.bo.TaskANOTICE1001BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1004BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1005BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.ServerOffline;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.repository.ServerErrorTraceRepository;
import cn.azhicloud.olserv.repository.ServerOfflineRepository;
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

    private final ServerErrorTraceRepository serverErrorTraceRepository;
    private final ServerOfflineRepository serverOfflineRepository;
    private final TodoTaskBaseService todoTaskBaseService;
    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK1005BO taskBO = JSON.parseObject(taskData, TaskTASK1005BO.class);

        Shadowbox box = Shadowbox.of(taskBO.getServerId());

        LocalDateTime now = LocalDateTime.now();
        // 如果一小时内错误超过5条，触发离线
        if (serverErrorTraceRepository.countByServerIdAndCreatedAtAfter(
                box.getApiUrl(), now.minusHours(1)) > 5) {
            // 托管态自动更新
            box.setOffline(Boolean.TRUE);

            // 获取服务器的离线记录
            List<ServerOffline> offlineList = serverOfflineRepository
                    .findByServerIdOrderByCreatedAtDesc(box.getApiUrl());

            TimeWheel tw;
            if (CollectionUtils.isNotEmpty(offlineList)) {
                // 根据最新一条离线记录计算时间轮位置
                tw = TimeWheelHelper.get(
                        offlineList.get(0).getOfflineDurations(),
                        offlineList.get(0).getDurationTimeunit());
            } else {
                tw = TimeWheelHelper.get();
            }

            // 保存离线记录
            ServerOffline offline = new ServerOffline();
            offline.setServerId(box.getApiUrl());
            offline.setOfflineDurations(tw.getNextTimes());
            offline.setDurationTimeunit(tw.getNextUnit());
            serverOfflineRepository.save(offline);

            // 发布自动任务 ANOTICE1001 通知运营
            autoTaskANOTICE1001BO(offline.getId());

            // 发布待办任务修改服务器为在线状态
            TaskTASK1004BO newTaskBO = new TaskTASK1004BO();
            newTaskBO.setServerId(box.getApiUrl());
            newTaskBO.setOffline(Boolean.FALSE);
            todoTaskBaseService.createTodoTaskAndPublishMQ(
                    TaskTypeConst.UPDATE_SHADOWBOX_OFFLINE_STATUS,
                    JSON.toJSONString(newTaskBO),
                    tw.getNextTimes(),
                    TimeUnit.valueOf(tw.getNextUnit()));
        }
    }

    private void autoTaskANOTICE1001BO(Long offlineId) {
        TaskANOTICE1001BO taskBO = new TaskANOTICE1001BO();
        taskBO.setOfflineId(offlineId);
        autoTaskBaseService.createAutoTaskAndPublishMQ(
                TaskTypeConst.A_NOTICE_SERVER_PASSIVE_OFFLINE, JSON.toJSONString(taskBO));
    }
}


