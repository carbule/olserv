package cn.azhicloud.olserv.autotask.noticetask;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.infra.helper.MailHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Subscribe;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.SubscribeRepository;
import cn.azhicloud.olserv.autotask.bo.TaskNOTICE1001BO;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private final AccountRepository accountRepository;

    private final SubscribeRepository subscribeRepository;

    private final MailHelper mailHelper;

    @Override
    public void execute(String taskData) {
        TaskNOTICE1001BO taskBO = JSON.parseObject(taskData, TaskNOTICE1001BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));
        Subscribe subscribe = subscribeRepository.findByAccountId(account.getId());
        if (subscribe == null) {
            throw BizException.format("账户 %s 未生成订阅", account.getUsername());
        }
        if (Boolean.TRUE.equals(account.getEnableNotice())) {
            if (StringUtils.isBlank(account.getEmail())) {
                throw BizException.format("账户 %s 没配置邮箱", account.getUsername());
            }
            mailHelper.sendSubscribeNotice(account.getEmail(), "ACCOUNT CREATED",
                    String.format("账户已创建，订阅链接：%s", subscribe.getShortLink()));
        }
    }
}
