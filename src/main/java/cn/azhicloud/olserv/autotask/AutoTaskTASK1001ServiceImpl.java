package cn.azhicloud.olserv.autotask;

import java.net.URI;
import java.util.List;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskNOTICE1001BO;
import cn.azhicloud.olserv.autotask.bo.TaskNOTICE1006BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1001BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 11:37
 */
@Service(TaskTypeConst.ALLOCATE_ACCOUNT_TO_SHADOWBOXES)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK1001ServiceImpl implements AutoTaskExecuteService {

    private final OutlineRepository outlineRepository;

    private final AccountRepository accountRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void execute(String taskData) {
        TaskTASK1001BO taskBO = JSON.parseObject(taskData, TaskTASK1001BO.class);

        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> BizException.format("账户不存在"));

        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();
        // 拆分成异步任务执行
        ExecutorHelper.execute(shadowboxes, box -> {
                    outlineRepository.createAccessKey(URI.create(box.getApiUrl()), account.getUsername());
                }, 3,
                ex -> log.error("创建 Key 失败：{}", ex.getMessage()));

        if (taskBO.isResetTraffic()) {
            // 创建自动任务 NOTICE1006 通知用户流量已重置
            TaskNOTICE1006BO newTaskBO = new TaskNOTICE1006BO();
            newTaskBO.setAccountId(account.getId());
            autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.NOTICE_ACCOUNT_TRAFFIC_RESET,
                    JSON.toJSONString(newTaskBO));
        } else {
            // 创建自动任务 NOTICE1001 通知用户账户已创建
            TaskNOTICE1001BO newTaskBO = new TaskNOTICE1001BO();
            newTaskBO.setAccountId(account.getId());
            autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.NOTICE_ACCOUNT_CREATED,
                    JSON.toJSONString(newTaskBO));
        }
    }
}
