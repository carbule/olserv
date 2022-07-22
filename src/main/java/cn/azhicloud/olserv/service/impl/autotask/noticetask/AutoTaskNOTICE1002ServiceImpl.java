package cn.azhicloud.olserv.service.impl.autotask.noticetask;

import java.util.List;

import cn.azhicloud.infra.exception.BizException;
import cn.azhicloud.infra.helper.ExecutorHelper;
import cn.azhicloud.infra.helper.MailHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskNOTICE1002BO;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskExecuteService;
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

    private final MailHelper mailHelper;

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
            mailHelper.sendSubscribeNotice(account.getEmail(), "ALLOCATE NEW NODE",
                    String.format("新节点 [%s] 添加，可刷新订阅获取", shadowbox.getName()));
        });
    }
}
