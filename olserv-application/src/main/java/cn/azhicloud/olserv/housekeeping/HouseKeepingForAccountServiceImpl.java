package cn.azhicloud.olserv.housekeeping;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.housekeeping.service.HouseKeepingService;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
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
        autoTaskBaseService.createAutoTaskAndPublicMQ(
                TaskTypeConst.DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT, null);
    }
}
