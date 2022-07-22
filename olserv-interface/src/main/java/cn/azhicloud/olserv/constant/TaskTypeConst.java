package cn.azhicloud.olserv.constant;

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
     * 在所有的 Shadowbox 上分配 Key
     */
    String ALLOCATE_ACCOUNT_TO_SHADOWBOXES = "TASK1001";

    /**
     * 为所有的 Account 分配 Key
     */
    String ALLOCATE_SHADOWBOX_TO_ACCOUNTS = "TASK2001";

    /**
     * 账户获取订阅发送通知
     */
    String ACCOUNT_PULL_SUBSCRIBE_NOTICE = "TASK2002";

    /**
     * 通知用户账户已经创建
     */
    String NOTICE_ACCOUNT_CREATED = "NOTICE1001";

    /**
     * 通知所有用户已经分配了新的 Key
     */
    String NOTICE_ACCOUNT_ALLOCATE_NEW_KEY = "NOTICE1002";

    /**
     * 通知用户流量即将用尽
     */
    String NOTICE_ACCOUNT_TRAFFIC_WARNING = "NOTICE1003";

    /**
     * 通知用户账户已过期
     */
    String NOTICE_ACCOUNT_EXPIRED = "NOTICE1004";

    /**
     * 通知用户流量已经用尽
     */
    String NOTICE_ACCOUNT_TRAFFIC_OVERED = "NOTICE1005";

    /**
     * 通知用户流量已重置
     */
    String NOTICE_ACCOUNT_TRAFFIC_RESET = "NOTICE1006";
}
