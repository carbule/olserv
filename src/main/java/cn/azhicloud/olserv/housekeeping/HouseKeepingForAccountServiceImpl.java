package cn.azhicloud.olserv.housekeeping;

import cn.azhicloud.infra.housekeeping.service.HouseKeepingService;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 针对过期的账户，进行订阅清理
 *
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
        autoTaskBaseService.createAutoTaskAndPublishMQ(
                TaskTypeConst.DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT, null);
    }
}
