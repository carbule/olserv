package cn.azhicloud.olserv.autotask;

import java.net.URI;
import java.util.List;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1003BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.domain.entity.Subscribe;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.repository.SubscribeRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/8/1 11:23
 */
@Service(TaskTypeConst.UNALLOCATE_ACCOUNT_TO_SHADOWBOXES)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK1003ServiceImpl implements AutoTaskExecuteService {

    private final OutlineRepository outlineRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final AccountRepository accountRepository;

    private final SubscribeRepository subscribeRepository;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK1003BO taskBO = JSON.parseObject(taskData, TaskTASK1003BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));

        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();
        ExecutorHelper.execute(shadowboxes, shadowbox -> {
                    outlineRepository.deleteAccessKey(URI.create(shadowbox.getApiUrl()), account.getUsername());
                }, 3,
                ex -> log.error("删除 Key 失败：{}", ex.getMessage()));

        accountRepository.delete(account);
        Subscribe subscribe = subscribeRepository.findByAccountId(account.getId());
        if (subscribe == null) {
            throw BizException.format("账户 %s 未生成订阅", account.getUsername());
        }
        subscribeRepository.delete(subscribe);
    }
}
