package cn.azhicloud.olserv.service.impl.autotask.hkptask;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.mapper.AccountMapper;
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1004BO;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 删除账户
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 21:47
 */
@Service(TaskTypeConst.DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT)
@Slf4j
@RequiredArgsConstructor
public class AutoTaskHKP1001ServiceImpl implements AutoTaskExecuteService {

    private final AccountMapper accountMapper;

    private final AccountRepository accountRepository;

    private final OutlineRepository outlineRepository;

    private final AccountService accountService;

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void execute(String taskData) {
        List<Account> expiredAccounts = accountMapper.selectExpiredAccounts();
        if (expiredAccounts.size() > 0) {
            expiredAccounts.forEach(account -> deleteAccessKey(account.getId()));
            accountRepository.deleteAllByIdInBatch(
                    expiredAccounts.stream().map(Account::getId)
                            .collect(Collectors.toList())
            );
            // 创建自动任务 NOTICE1004 发送邮件通知
            for (Account account : expiredAccounts) {
                TaskNOTICE1004BO taskBO = new TaskNOTICE1004BO();
                taskBO.setAccountId(account.getId());
                autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.NOTICE_ACCOUNT_EXPIRED,
                        JSON.toJSONString(taskBO));
            }
            return;
        }
        log.info("No expired accounts.");
    }

    private void deleteAccessKey(String accountId) {
        List<Shadowbox> shadowboxes = accountService.listShadowboxOwnedByAccount(accountId);
        for (Shadowbox box : shadowboxes) {
            for (AccessKey key : box.getAccessKeys()) {
                outlineRepository.deletesAnAccessKey(URI.create(box.getApiUrl()), key.getId());
            }
        }
    }
}
