package cn.azhicloud.olserv.service.impl.autotask.bo;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @see TaskTypeConst#NOTICE_ACCOUNT_TRAFFIC_OVERED
 * @since 2022/7/22 15:26
 */
@Data
public class TaskNOTICE1005BO {

    /**
     * 账户 ID
     */
    private String accountId;
}
