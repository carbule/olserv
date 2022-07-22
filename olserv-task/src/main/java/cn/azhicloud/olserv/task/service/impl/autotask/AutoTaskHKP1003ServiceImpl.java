package cn.azhicloud.olserv.task.service.impl.autotask;

import java.util.List;

import cn.azhicloud.olserv.task.constant.TaskTypeConst;
import cn.azhicloud.olserv.task.repository.AutoTaskRepository;
import cn.azhicloud.olserv.task.repository.mapper.AutoTaskMapper;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.task.service.impl.autotask.bo.TaskHKP1003BO;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 21:42
 */
@Service(TaskTypeConst.DO_HOUSEKEEPING_FOR_FINISHED_AUTO_TASK)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskHKP1003ServiceImpl implements AutoTaskExecuteService {

    private final AutoTaskMapper autoTaskMapper;

    private final AutoTaskRepository autoTaskRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String taskData) {
        TaskHKP1003BO taskBO = JSON.parseObject(taskData, TaskHKP1003BO.class);

        List<Long> taskIds = autoTaskMapper.selectFinishedTaskIdsBeforeDays(taskBO.getHousekeepingDays());
        autoTaskRepository.deleteAllByIdInBatch(taskIds);
    }
}
