package cn.azhicloud.olserv.task.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.infra.helper.MailHelper;
import cn.azhicloud.olserv.task.constant.Executor;
import cn.azhicloud.olserv.task.constant.TaskStatus;
import cn.azhicloud.olserv.task.model.entity.AutoTask;
import cn.azhicloud.olserv.task.model.entity.AutoTaskCfg;
import cn.azhicloud.olserv.task.repository.AutoTaskCfgRepository;
import cn.azhicloud.olserv.task.repository.AutoTaskRepository;
import cn.azhicloud.olserv.task.service.AutoTaskBizService;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
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
            // 如果任务已禁用，则取消任务执行
            if (!Boolean.TRUE.equals(taskType.getEnabled())) {
                log.info("The task type {} is disabled, cancel task {} execute.", taskType.getTaskType(), task.getTaskNo());
                task.setStatus(TaskStatus.CANCELLED.value);
                autoTaskRepository.save(task);
                return;
            }

            AutoTaskExecuteService executeService = context.getBean(taskType.getExecServiceId(),
                    AutoTaskExecuteService.class);

            // 开始执行
            Future<?> submit = Executors.newCachedThreadPool().submit(() ->
                    executeService.execute(task.getTaskData()));
            // 任务执行如果超过 5 分钟，判定超时，中断该任务并抛出异常
            try {
                submit.get(5, TimeUnit.MINUTES);
            } catch (Exception e) {
                submit.cancel(Boolean.TRUE);
                throw e;
            }
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
