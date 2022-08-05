package cn.azhicloud.olserv.autotask.bo;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @see TaskTypeConst#NOTICE_ACCOUNT_TRAFFIC_RESET
 * @since 2022/7/22 15:26
 */
@Data
public class TaskNOTICE1006BO {

    /**
     * 账户 ID
     */
    private String accountId;
}
