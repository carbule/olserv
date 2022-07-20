package cn.azhicloud.task.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import cn.azhicloud.infra.exception.BizException;
import cn.azhicloud.infra.helper.MailHelper;
import cn.azhicloud.task.constant.Executor;
import cn.azhicloud.task.constant.TaskStatus;
import cn.azhicloud.task.model.entity.AutoTask;
import cn.azhicloud.task.model.entity.AutoTaskCfg;
import cn.azhicloud.task.repository.AutoTaskCfgRepository;
import cn.azhicloud.task.repository.AutoTaskRepository;
import cn.azhicloud.task.service.AutoTaskBizService;
import cn.azhicloud.task.service.AutoTaskExecuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 18:00
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AutoTaskBizServiceImpl implements AutoTaskBizService {

    private final AutoTaskRepository autoTaskRepository;

    private final AutoTaskCfgRepository autoTaskCfgRepository;

    private final ApplicationContext context;

    private final MailHelper mailHelper;

    @Override
    public void execute(String taskNo, String executor) {
        AutoTask task = autoTaskRepository.findByTaskNoAndStatus(taskNo, TaskStatus.PENDING.value);
        if (task == null) {
            throw BizException.format("任务 %s 不存在", taskNo);
        }

        // 更新任务为处理中
        task.setStatus(TaskStatus.PROCESSING.value);
        task.setExecutor(executor);
        task.setExecuteStartAt(LocalDateTime.now());
        autoTaskRepository.save(task);

        try {
            AutoTaskCfg taskType = autoTaskCfgRepository.findByTaskType(task.getTaskType());
            if (taskType == null) {
                throw BizException.format("不支持该任务类型：%s", task.getTaskType());
            }

            AutoTaskExecuteService executeService = context.getBean(taskType.getExecServiceId(),
                    AutoTaskExecuteService.class);

            // 开始执行
            executeService.execute(task.getTaskData());
        } catch (Exception e) {
            log.error("auto task execute failed", e);
            updateTaskToFailed(task, e);
            mailHelper.sendAlarmMail("AUTO TASK EXECUTE FAILED", ExceptionUtils.getStackTrace(e));
            return;
        }

        // 更新任务为已完成
        task.setStatus(TaskStatus.FINISHED.value);
        task.setExecuteEndAt(LocalDateTime.now());
        autoTaskRepository.save(task);
    }

    /**
     * 使用异步，防止处理时间过长导致调度任务超时
     */
    @Override
    @Async
    public void executeByJob() {
        List<AutoTask> tasks = autoTaskRepository.findByStatus(TaskStatus.PENDING.value);

        tasks.forEach(t -> execute(t.getTaskNo(), Executor.JOB.name()));
    }

    private String getExMessage(Exception e) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        if (stackTrace.length() > AutoTask.FIELD_FAILED_REASON_LENGTH) {
            return stackTrace.substring(0, AutoTask.FIELD_FAILED_REASON_LENGTH);
        }
        return stackTrace;
    }

    private void updateTaskToFailed(AutoTask task, Exception e) {
        // 业务异常和系统异常状态非一致
        task.setStatus(e instanceof BizException ?
                TaskStatus.FAILED.value : TaskStatus.ERROR.value);
        task.setFailedReason(getExMessage(e));
        task.setExecuteEndAt(LocalDateTime.now());
        autoTaskRepository.save(task);
    }
}
