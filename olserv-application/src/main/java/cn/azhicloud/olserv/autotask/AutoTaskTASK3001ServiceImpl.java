package cn.azhicloud.olserv.autotask;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import cn.azhicloud.olserv.autotask.bo.TaskTASK3001BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Subscribe;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.SubscribeRepository;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 22:51
 */
@Service(TaskTypeConst.GENERATE_SUBSCRIBE_AND_SHORT_URL)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK3001ServiceImpl implements AutoTaskExecuteService {

    @Value("${template.subscribe-link}")
    private String subscribeLinkTMPL;

    private final AccountRepository accountRepository;

    private final SubscribeRepository subscribeRepository;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK3001BO taskBO = JSON.parseObject(taskData, TaskTASK3001BO.class);

        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));

        Subscribe subscribe = subscribeRepository.findByAccountId(account.getId());
        if (subscribe != null) {
            throw BizException.format("账户 %s 已经生成订阅", account.getUsername());
        }

        subscribe = new Subscribe();
        subscribe.setCreatedAt(LocalDateTime.now());
        subscribe.setAccountId(account.getId());
        subscribe.setSubscribeLink(MessageFormat.format(subscribeLinkTMPL, account.getId()));

        subscribeRepository.save(subscribe);
    }
}
