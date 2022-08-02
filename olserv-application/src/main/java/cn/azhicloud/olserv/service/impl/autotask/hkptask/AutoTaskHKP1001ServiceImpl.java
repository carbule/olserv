package cn.azhicloud.olserv.service.impl.autotask.hkptask;

import java.util.List;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.mapper.AccountMapper;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1004BO;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskTASK1003BO;
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

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void execute(String taskData) {
        List<Account> expiredAccounts = accountMapper.selectExpiredAccounts();
        if (expiredAccounts.size() > 0) {
            for (Account account : expiredAccounts) {
                {
                    // 创建自动任务 NOTICE1004 发送邮件通知
                    TaskNOTICE1004BO taskBO = new TaskNOTICE1004BO();
                    taskBO.setAccountId(account.getId());
                    autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.NOTICE_ACCOUNT_EXPIRED,
                            JSON.toJSONString(taskBO));
                }
                {
                    // 创建自动任务 TASK1003 删除账户
                    TaskTASK1003BO taskBO = new TaskTASK1003BO();
                    taskBO.setAccountId(account.getId());
                    autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.UNALLOCATE_ACCOUNT_TO_SHADOWBOXES,
                            JSON.toJSONString(taskBO));
                }
            }
            return;
        }
        log.info("No expired accounts.");
    }
}
