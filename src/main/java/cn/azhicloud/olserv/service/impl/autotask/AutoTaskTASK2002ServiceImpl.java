package cn.azhicloud.olserv.service.impl.autotask;

import cn.azhicloud.infra.exception.BizException;
import cn.azhicloud.infra.helper.MailHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskTASK2002BO;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final MailHelper mailHelper;

    private final AccountRepository accountRepository;

    @Override
    public void execute(String taskData) {
        TaskTASK2002BO taskBO = JSON.parseObject(taskData, TaskTASK2002BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));

        mailHelper.sendAlarmMail("ACCOUNT PULL SUBSCRIBE",
                String.format("account [%s] pull subscribe", account.getUsername()));
    }
}
