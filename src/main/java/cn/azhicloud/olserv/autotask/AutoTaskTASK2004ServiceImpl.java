package cn.azhicloud.olserv.autotask;

import javax.transaction.Transactional;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.model.IPUserAgentInfoResponse;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2004BO;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2005BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.PullHistory;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.PullHistoryRepository;
import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/25 18:10
 */
@Service(TaskTypeConst.SAVE_PULL_HISTORY_LOCATION)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK2004ServiceImpl implements AutoTaskExecuteService {

    private final AccountRepository accountRepository;
    private final PullHistoryRepository pullHistoryRepository;
    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK2004BO taskBO = JSON.parseObject(taskData, TaskTASK2004BO.class);

        PullHistory history = PullHistory.of(taskBO.getHistoryId());
        if (StringUtils.isBlank(history.getFromIp())) {
            throw new BizException("访问者 IP 地址为空");
        }

        // 忽略 ipv6
        // 如果是内网 IP，无法获取地理位置
        if (history.getFromIp().length() <= 15 && !NetUtil.isInnerIP(history.getFromIp())) {
            history.setFromLocation(IPUserAgentInfoResponse.of(history.getFromIp())
                    .locationString());
        }

        // 发布自动任务 TASK2005 进行邮件通知
        autoTaskTASK2005(history.getId());
    }

    private void autoTaskTASK2005(Long historyId) {
        TaskTASK2005BO taskBO = new TaskTASK2005BO();
        taskBO.setHistoryId(historyId);
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.PULL_HISTORY_NOTICE,
                JSON.toJSONString(taskBO));
    }
}
