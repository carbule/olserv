package cn.azhicloud.task.constant;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 12:54
 */
public interface TaskTypeConst {

    /**
     * 处理过期账户
     */
    String DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT = "HKP1001";

    /**
     * 处理流量用尽的账户
     */
    String DO_HOUSEKEEPING_FOR_OUT_OF_TRAFFIC_ACCOUNT = "HKP1002";

    /**
     * 处理已经处理完成的自动任务
     */
    String DO_HOUSEKEEPING_FOR_FINISHED_AUTO_TASK = "HKP1003";

    /**
     * 在所有的 Shadowbox 上分配 Key
     */
    String ALLOCATE_ACCOUNT_TO_SHADOWBOXES = "TASK1001";

    /**
     * 为所有的 Account 分配 Key
     */
    String ALLOCATE_SHADOWBOX_TO_ACCOUNTS = "TASK2001";
}
