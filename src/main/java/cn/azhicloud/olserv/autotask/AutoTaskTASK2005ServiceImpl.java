package cn.azhicloud.olserv.autotask;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2005BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.constant.em.NotifyStatusEnum;
import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.PullHistory;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.helper.MailHelperExtra;
import cn.azhicloud.olserv.repository.PullHistoryRepository;
import cn.azhicloud.olserv.repository.mapper.PullHistoryMapper;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/18 13:46
 */
@Service(TaskTypeConst.PULL_HISTORY_NOTICE)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK2005ServiceImpl implements AutoTaskExecuteService {

    /**
     * 通知中的账户集
     */
    private static final Set<String> NOTIFYING_ACCOUNT_SET = new ConcurrentHashSet<>();

    @Value("${alarm.mail.receiver}")
    private String alarmMailReceiver;
    private final MailHelperExtra mailHelperExtra;
    private final PullHistoryRepository pullHistoryRepository;
    private final PullHistoryMapper pullHistoryMapper;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK2005BO taskBO = JSON.parseObject(taskData, TaskTASK2005BO.class);

        PullHistory history = PullHistory.of(taskBO.getHistoryId());
        if (!NotifyStatusEnum.READY.name().equals(history.getNotifyStatus())) {
            throw BizException.format("PullHistory(%s) 实体状态异常; 期望: %s, 实际: %s",
                    history.getId(), NotifyStatusEnum.READY.name(), history.getNotifyStatus());
        }
        Account account = Account.of(history.getAccountId());

        // 如果存在正在通知的账户，取消本次通知
        if (NOTIFYING_ACCOUNT_SET.contains(account.getId())) {
            history.setNotifyStatus(NotifyStatusEnum.CANCELLED.name());
            log.info("触发通知合并，本次通知取消; 实体: {}", JSON.toJSONString(history));
            return;
        }

        // 查找该账户一分钟内的拉取记录
        List<PullHistory> histories = pullHistoryMapper.selectHistoriesWithinOneMin(history.getAccountId());

        // 一分钟内，多条拉取记录只通知一次
        if (histories.stream().anyMatch(e ->
                NotifyStatusEnum.NOTIFIED.name().equals(e.getNotifyStatus()))) {
            history.setNotifyStatus(NotifyStatusEnum.CANCELLED.name());
            log.info("触发通知合并，本次通知取消; 实体: {}", JSON.toJSONString(history));
            return;
        }

        // 准备通知
        NOTIFYING_ACCOUNT_SET.add(account.getId());
        List<Shadowbox> boxes = JSON.parseArray(history.getPullContent(), Shadowbox.class);
        String nodes = boxes.stream().map(Shadowbox::getName)
                .collect(Collectors.joining(","));

        if (Boolean.TRUE.equals(account.getEnableNotice())) {
            String EMAIL_TEMPLATE = "账户 %s 拉取订阅\r\n节点数：%s \r\n访问地址：%s %s";
            mailHelperExtra.sendSubscribeNotice(alarmMailReceiver, "ACCOUNT PULL SUBSCRIBE",
                    String.format(EMAIL_TEMPLATE,
                            account.getUsername(),
                            nodes,
                            Optional.ofNullable(history.getFromIp()).orElse(""),
                            Optional.ofNullable(history.getFromLocation()).orElse("")));
        }
        history.setNotifyStatus(NotifyStatusEnum.NOTIFIED.name());
        // 通知结束，移除正在通知列表
        NOTIFYING_ACCOUNT_SET.remove(account.getId());
    }
}
