package cn.azhicloud.olserv.service.impl.autotask.noticetask;

import cn.azhicloud.infra.exception.BizException;
import cn.azhicloud.infra.helper.MailHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1001BO;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/22 09:49
 */
@Service(TaskTypeConst.NOTICE_ACCOUNT_CREATED)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskNOTICE1001ServiceImpl implements AutoTaskExecuteService {

    @Value("${url-template.account-subscribe}")
    private String accountSubscribeUrlTemplate;

    private final AccountRepository accountRepository;

    private final MailHelper mailHelper;

    @Override
    public void execute(String taskData) {
        TaskNOTICE1001BO taskBO = JSON.parseObject(taskData, TaskNOTICE1001BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));
        if (StringUtils.isBlank(account.getEmail())) {
            throw BizException.format("账户 %s 没配置邮箱", account.getUsername());
        }
        String subscribe = accountSubscribeUrlTemplate.replace("{accountId}", account.getId());
        mailHelper.sendSubscribeNotice(account.getEmail(), "ACCOUNT CREATED",
                String.format("账户已创建，订阅链接：%s", subscribe));
    }
}
