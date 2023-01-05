package cn.azhicloud.olserv.autotask;

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

        Shadowbox box = Shadowbox.of(taskBO.getServerId());
        box.setOffline(taskBO.getOffline());
        shadowboxRepository.save(box);
    }
}
