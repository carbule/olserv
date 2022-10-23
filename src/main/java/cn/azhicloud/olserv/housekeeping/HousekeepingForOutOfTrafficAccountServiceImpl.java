package cn.azhicloud.olserv.housekeeping;

import cn.azhicloud.infra.housekeeping.service.HouseKeepingService;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 统计账户的使用流量，并针对已用尽流量的账户进行订阅清理
 *
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
    public void doHousekeeping() {
        autoTaskBaseService.createAutoTaskAndPublishMQ(
                TaskTypeConst.DO_HOUSEKEEPING_FOR_OUT_OF_TRAFFIC_ACCOUNT, null);
    }
}
