package cn.azhicloud.olserv.autotask;

import java.util.Optional;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK1004BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 15:00
 */
@Service(TaskTypeConst.UPDATE_SHADOWBOX_OFFLINE_STATUS)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK1004ServiceImpl implements AutoTaskExecuteService {

    private final ShadowboxRepository shadowboxRepository;

    @Override
    public void execute(String taskData) {
        TaskTASK1004BO taskBO = JSON.parseObject(taskData, TaskTASK1004BO.class);

        Optional<Shadowbox> optional = shadowboxRepository.findById(taskBO.getServerId());

        if (optional.isPresent()) {
            optional.get().setOffline(taskBO.getOffline());
            shadowboxRepository.save(optional.get());
            return;
        }

        // 如果是 离线->在线，可以允许记录不存在的情况，因为可能人工已经主动下线
        if (Boolean.TRUE.equals(taskBO.getOffline())) {
            throw BizException.format("服务器 %s 不存在", taskBO.getServerId());
        }
    }
}
