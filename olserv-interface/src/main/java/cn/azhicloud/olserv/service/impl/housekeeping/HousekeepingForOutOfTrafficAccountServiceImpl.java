package cn.azhicloud.olserv.service.impl.housekeeping;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.housekeeping.service.HouseKeepingService;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 14:31
 */
@Service("housekeepingForOutOfTrafficAccountServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class HousekeepingForOutOfTrafficAccountServiceImpl implements HouseKeepingService {

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    @Transactional
    public void doHousekeeping() {
        autoTaskBaseService.createAutoTaskAndPublicMQ(
                TaskTypeConst.DO_HOUSEKEEPING_FOR_OUT_OF_TRAFFIC_ACCOUNT, null);
    }
}
