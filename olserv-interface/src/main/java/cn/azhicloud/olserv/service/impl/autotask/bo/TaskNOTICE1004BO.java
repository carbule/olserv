package cn.azhicloud.olserv.service.impl.autotask.bo;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @see TaskTypeConst#NOTICE_ACCOUNT_EXPIRED
 * @since 2022/7/22 15:26
 */
@Data
public class TaskNOTICE1004BO {

    /**
     * 账户 ID
     */
    private String accountId;
}
