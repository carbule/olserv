package cn.azhicloud.olserv.autotask;

import java.net.URI;
import java.util.List;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskNOTICE1002BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2001BO;
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
 * @since 2022/7/17 11:43
 */
@Service(TaskTypeConst.ALLOCATE_SHADOWBOX_TO_ACCOUNTS)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK2001ServiceImpl implements AutoTaskExecuteService {

    private final OutlineRepository outlineRepository;

    private final AccountRepository accountRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void execute(String taskData) {
        TaskTASK2001BO taskBO = JSON.parseObject(taskData, TaskTASK2001BO.class);

        Shadowbox shadowbox = shadowboxRepository.findById(taskBO.getApiUrl())
                .orElseThrow(() -> BizException.format("Shadowbox 不存在"));
        List<Account> accounts = accountRepository.findAll();
        // 拆分异步任务执行
        ExecutorHelper.execute(accounts, account -> {
                    outlineRepository.createAccessKey(URI.create(shadowbox.getApiUrl()), account.getUsername());
                }, 3,
                ex -> log.error("创建 Key 失败：{}", ex.getMessage()));

        // 创建自动任务 NOTICE1002 通知所有用户拉取最新订阅
        TaskNOTICE1002BO newTaskBO = new TaskNOTICE1002BO();
        newTaskBO.setApiUrl(shadowbox.getApiUrl());
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.NOTICE_ACCOUNT_ALLOCATE_NEW_KEY,
                JSON.toJSONString(newTaskBO));
    }
}
