package cn.azhicloud.olserv.autotask.hkptask;

import java.util.List;

import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.audit.AuditTaskTypeConst;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/8/6 11:03
 */
@Service(TaskTypeConst.CACHE_ACCOUNT_OWNED_KEYS)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskHKP1005ServiceImpl implements AutoTaskExecuteService {

    private final OutlineRepository outlineRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final AccountRepository accountRepository;

    private final AutoTaskBaseService autoTaskBaseService;

    private static final int RETRY_TIME = 3;

    @Override
    public void execute(String taskData) {
        List<Account> accounts = accountRepository.findAll();
        List<Shadowbox> shadowboxes = shadowboxRepository.findByOfflineIsFalse();

        if (accounts.isEmpty() || shadowboxes.isEmpty()) {
            return;
        }

        // 清空缓存
        outlineRepository.clearCache();

        ExecutorHelper.execute(shadowboxes, shadowbox -> {
                    outlineRepository.returnsInformationAboutTheServer(shadowbox.URI());
                }, RETRY_TIME,
                ex -> log.error("缓存服务器信息失败：{}", ex.getMessage()));


        ExecutorHelper.execute(accounts, shadowboxes, (account, shadowbox) -> {
                    outlineRepository.getAccessKey(shadowbox.URI(), account.getUsername());
                }, RETRY_TIME,
                ex -> log.error("缓存 Key 信息失败：{}", ex.getMessage()));

        // 发布审计相关任务
        autoTaskBaseService.createAutoTaskAndPublishMQ(
                AuditTaskTypeConst.PERSISTENT_ACCOUNT_OWNED_KEYS, null);
    }
}
