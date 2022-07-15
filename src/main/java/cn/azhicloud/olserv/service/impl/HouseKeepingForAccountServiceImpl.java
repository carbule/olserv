package cn.azhicloud.olserv.service.impl;

import cn.azhicloud.housekeeping.service.HouseKeepingService;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/17 13:05
 */
@Service("houseKeepingForAccountServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class HouseKeepingForAccountServiceImpl implements HouseKeepingService {

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public void doHousekeeping() {
        autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst
                .DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT, null);
    }
}
