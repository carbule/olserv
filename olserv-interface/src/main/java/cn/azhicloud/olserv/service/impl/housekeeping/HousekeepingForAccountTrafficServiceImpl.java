package cn.azhicloud.olserv.service.impl.housekeeping;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.housekeeping.service.HouseKeepingService;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/27 14:25
 */
@Service("housekeepingForAccountTrafficServiceImpl")
@RequiredArgsConstructor
public class HousekeepingForAccountTrafficServiceImpl implements HouseKeepingService {

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void doHousekeeping() {
        autoTaskBaseService.createAutoTaskAndPublicMQ(
                TaskTypeConst.STATS_ACCOUNT_USED_TRAFFIC_OF_DAILY, null);
    }
}
