package cn.azhicloud.olserv.service.impl.autotask.noticetask;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.infra.helper.MailHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1003BO;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
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
@Service(TaskTypeConst.NOTICE_ACCOUNT_TRAFFIC_WARNING)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskNOTICE1003ServiceImpl implements AutoTaskExecuteService {

    private final AccountRepository accountRepository;

    private final MailHelper mailHelper;

    @Override
    public void execute(String taskData) {
        TaskNOTICE1003BO taskBO = JSON.parseObject(taskData, TaskNOTICE1003BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));
        if (Boolean.TRUE.equals(account.getEnableNotice())) {
            if (StringUtils.isBlank(account.getEmail())) {
                throw BizException.format("账户 %s 未配置邮箱", account.getUsername());
            }
            mailHelper.sendSubscribeNotice(account.getEmail(), "TRAFFIC WARNING",
                    "剩余流量不到 1G，如需继续使用尽快联系运维进行重置");
        }
    }
}
