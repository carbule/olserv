package cn.azhicloud.olserv.service.impl.autotask.bo;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 11:38
 */
@Data
public class TaskTASK1001BO {

    /**
     * 是否是重置流量操作
     */
    private boolean resetTraffic = false;

    /**
     * 账户 ID
     */
    private String accountId;
}
