package cn.azhicloud.olserv.autotask.noticetask;

import java.util.List;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskNOTICE1002BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.helper.MailHelperExtra;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/22 09:49
 */
@Service(TaskTypeConst.NOTICE_ACCOUNT_ALLOCATE_NEW_KEY)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskNOTICE1002ServiceImpl implements AutoTaskExecuteService {

    private final AccountRepository accountRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final MailHelperExtra mailHelperExtra;

    @Override
    public void execute(String taskData) {
        TaskNOTICE1002BO taskBO = JSON.parseObject(taskData, TaskNOTICE1002BO.class);
        Shadowbox shadowbox = shadowboxRepository.findById(taskBO.getApiUrl())
                .orElseThrow(() -> new BizException("服务器不存在"));
        List<Account> accounts = accountRepository.findByEmailNotNull();
        if (CollectionUtils.isEmpty(accounts)) {
            log.info("没有已配置邮箱的账户");
            return;
        }

        ExecutorHelper.execute(accounts, account -> {
            if (Boolean.TRUE.equals(account.getEnableNotice())) {
                mailHelperExtra.sendSubscribeNotice(account.getEmail(), "ALLOCATE NEW NODE",
                        String.format("新节点 [%s] 添加，可刷新订阅获取", shadowbox.getName()));
            }
        });
    }
}
