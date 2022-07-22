package cn.azhicloud.olserv.service.impl.autotask.noticetask;

import cn.azhicloud.infra.exception.BizException;
import cn.azhicloud.infra.helper.MailHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1004BO;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/22 15:25
 */
@Service(TaskTypeConst.NOTICE_ACCOUNT_EXPIRED)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskNOTICE1004ServiceImpl implements AutoTaskExecuteService {

    private final AccountRepository accountRepository;

    private final MailHelper mailHelper;

    @Override
    public void execute(String taskData) {
        TaskNOTICE1004BO taskBO = JSON.parseObject(taskData, TaskNOTICE1004BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));
        if (StringUtils.isBlank(account.getEmail())) {
            throw BizException.format("账户 %s 未配置邮箱", account.getUsername());
        }
        mailHelper.sendSubscribeNotice(account.getEmail(), "ACCOUNT EXPIRED",
                "账户已过期，如需继续使用可联系运维");
    }
}
