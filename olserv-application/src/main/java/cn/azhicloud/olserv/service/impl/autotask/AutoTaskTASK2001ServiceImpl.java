package cn.azhicloud.olserv.service.impl.autotask;

import java.net.URI;
import java.util.List;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.infra.helper.ExecutorHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1002BO;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskTASK2001BO;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
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
            try {
                URI uri = URI.create(shadowbox.getApiUrl());
                // 新增一个 key
                AccessKey accessKey = outlineRepository.createsANewAccessKey(uri);

                // 设置 key 的名称
                AccessKey renameBody = new AccessKey();
                renameBody.setName(account.getUsername());
                try {
                    outlineRepository.renamesAnAccessKey(uri, accessKey.getId(), renameBody);
                } catch (Exception e) {
                    // 如果设置名称失败，删除先前创建的 key
                    outlineRepository.deletesAnAccessKey(uri, accessKey.getId());
                    throw e;
                }
            } catch (Exception e) {
                log.error("call api {} failed", shadowbox.getApiUrl(), e);
            }
        });

        // 创建自动任务 NOTICE1002 通知所有用户拉取最新订阅
        TaskNOTICE1002BO newTaskBO = new TaskNOTICE1002BO();
        newTaskBO.setApiUrl(shadowbox.getApiUrl());
        autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.NOTICE_ACCOUNT_ALLOCATE_NEW_KEY,
                JSON.toJSONString(newTaskBO));
    }
}
