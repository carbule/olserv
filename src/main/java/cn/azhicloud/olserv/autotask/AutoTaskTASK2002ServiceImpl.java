package cn.azhicloud.olserv.autotask;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2002BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.helper.MailHelperExtra;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/18 13:46
 */
@Service(TaskTypeConst.ACCOUNT_PULL_SUBSCRIBE_NOTICE)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK2002ServiceImpl implements AutoTaskExecuteService {

    @Value("${alarm.mail.receiver}")
    private String alarmMailReceiver;

    private final MailHelperExtra mailHelperExtra;

    private final AccountRepository accountRepository;

    @Override
    public void execute(String taskData) {
        TaskTASK2002BO taskBO = JSON.parseObject(taskData, TaskTASK2002BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));

        if (Boolean.TRUE.equals(account.getEnableNotice())) {
            mailHelperExtra.sendSubscribeNotice(alarmMailReceiver, "ACCOUNT PULL SUBSCRIBE",
                    String.format("Account [%s] pull subscribe, has nodes: %s", account.getUsername(),
                            String.join(", ", taskBO.getNodes())));
        }
    }
}
