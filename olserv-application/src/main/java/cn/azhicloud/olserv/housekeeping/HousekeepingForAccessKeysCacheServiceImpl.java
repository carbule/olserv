package cn.azhicloud.olserv.housekeeping;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.housekeeping.service.HouseKeepingService;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/8/6 11:23
 */
@Service("housekeepingForAccessKeysCacheServiceImpl")
@RequiredArgsConstructor
public class HousekeepingForAccessKeysCacheServiceImpl implements HouseKeepingService {

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void doHousekeeping() {
        autoTaskBaseService.createAutoTaskAndPublicMQ(
                TaskTypeConst.CACHE_ACCOUNT_OWNED_KEYS, null);
    }
}
