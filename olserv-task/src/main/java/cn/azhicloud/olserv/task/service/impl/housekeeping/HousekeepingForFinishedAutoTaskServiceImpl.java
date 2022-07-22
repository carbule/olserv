package cn.azhicloud.olserv.task.service.impl.housekeeping;

import java.util.Map;

import cn.azhicloud.olserv.housekeeping.service.HouseKeepingService;
import cn.azhicloud.olserv.task.constant.TaskTypeConst;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.task.service.impl.autotask.bo.TaskHKP1003BO;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 21:39
 */
@Slf4j
@Service("housekeepingForFinishedAutoTaskServiceImpl")
@RequiredArgsConstructor
public class HousekeepingForFinishedAutoTaskServiceImpl implements HouseKeepingService {

    /**
     * 参数：housekeepingDays
     */
    private static final String PARAM_HOUSEKEEPING_DAYS = "housekeepingDays";

    /**
     * 如果 PARAM_HOUSEKEEPING_LIMIT 未指定，默认处理 3 天前的数据
     */
    private static final int DEFAULT_HOUSEKEEPING_DAYS = 3;

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void doHousekeeping() {
        TaskHKP1003BO taskBO = new TaskHKP1003BO();
        taskBO.setHousekeepingDays(DEFAULT_HOUSEKEEPING_DAYS);

        autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.DO_HOUSEKEEPING_FOR_FINISHED_AUTO_TASK,
                JSON.toJSONString(taskBO));
    }

    @Override
    public void doHousekeeping(Map<String, Object> params) {
        int days = (int) params.getOrDefault(PARAM_HOUSEKEEPING_DAYS, DEFAULT_HOUSEKEEPING_DAYS);

        TaskHKP1003BO taskBO = new TaskHKP1003BO();
        taskBO.setHousekeepingDays(days);

        autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.DO_HOUSEKEEPING_FOR_FINISHED_AUTO_TASK,
                JSON.toJSONString(taskBO));
    }
}
