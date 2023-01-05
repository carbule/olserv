package cn.azhicloud.olserv.autotask.hkptask;

import java.util.List;

import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1005BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.ServerErrorTrace;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ServerErrorTraceRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 14:36
 */
@Service(TaskTypeConst.SERVER_HEALTH_MONITOR)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskHKP1006ServiceImpl implements AutoTaskExecuteService {

    private final ShadowboxRepository shadowboxRepository;
    private final OutlineRepository outlineRepository;
    private final ServerErrorTraceRepository serverErrorTraceRepository;
    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void execute(String taskData) {
        // 只获取在线的服务器
        List<Shadowbox> boxes = shadowboxRepository.findByOfflineIsFalse();

        ExecutorHelper.execute(boxes, box -> {
            try {
                outlineRepository.returnsInformationAboutTheServer(box.URI());
            } catch (Exception e) {
                // 保存异常堆栈
                ServerErrorTrace err = new ServerErrorTrace();
                err.setServerId(box.getApiUrl());
                err.setError(ExceptionUtils.getStackTrace(e));
                serverErrorTraceRepository.save(err);

                // 发布自动任务 TASK1005 做服务器离线策略
                TaskTASK1005BO newTaskBO = new TaskTASK1005BO();
                newTaskBO.setServerId(box.getApiUrl());
                autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.SHADOWBOX_OFFLINE_STRATEGY,
                        JSON.toJSONString(newTaskBO));
            }
        });
    }
}
