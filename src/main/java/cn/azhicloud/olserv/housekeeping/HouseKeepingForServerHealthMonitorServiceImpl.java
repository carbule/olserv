package cn.azhicloud.olserv.housekeeping;

import cn.azhicloud.infra.housekeeping.service.HouseKeepingService;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 17:47
 */
@Service("houseKeepingForServerHealthMonitorServiceImpl")
@RequiredArgsConstructor
public class HouseKeepingForServerHealthMonitorServiceImpl implements HouseKeepingService {

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void doHousekeeping() {
        autoTaskBaseService.createAutoTaskAndPublishMQ(
                TaskTypeConst.SERVER_HEALTH_MONITOR, null);
    }
}
