package cn.azhicloud.olserv.autotask.hkptask;

import java.util.List;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.helper.ExecutorHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
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

    @Override
    public void execute(String taskData) {
        List<Account> accounts = accountRepository.findAll();
        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();

        if (accounts.isEmpty() || shadowboxes.isEmpty()) {
            return;
        }

        // 清空缓存
        outlineRepository.clearCache();

        // 缓存服务器信息
        ExecutorHelper.execute(shadowboxes, shadowbox -> {
            outlineRepository.returnsInformationAboutTheServer(shadowbox.URI());
        });

        // account->shadowbox 维度拆分多线程任务缓存 Key 信息
        ExecutorHelper.execute(accounts, shadowboxes, (account, shadowbox) -> {
            try {
                outlineRepository.getAccessKey(shadowbox.URI(), account.getUsername());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
