package cn.azhicloud.olserv.constant;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 12:54
 */
public interface TaskTypeConst {

    // Housekeeping 任务

    String DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT = "HKP1001";
    String DO_HOUSEKEEPING_FOR_EXPIRED_ACCOUNT_DESC = "处理过期账户";

    String DO_HOUSEKEEPING_FOR_OUT_OF_TRAFFIC_ACCOUNT = "HKP1002";
    String DO_HOUSEKEEPING_FOR_OUT_OF_TRAFFIC_ACCOUNT_DESC = "处理流量用尽的账户";

    String CACHE_ACCOUNT_OWNED_KEYS = "HKP1005";
    String CACHE_ACCOUNT_OWNED_KEYS_DESC = "缓存用户拥有的 Key";

    // 服务器任务

    String ALLOCATE_ACCOUNT_TO_SHADOWBOXES = "TASK1001";
    String ALLOCATE_ACCOUNT_TO_SHADOWBOXES_DESC = "在所有的 Shadowbox 上分配 Key";

    String UNALLOCATE_ACCOUNT_TO_SHADOWBOXES = "TASK1003";
    String UNALLOCATE_ACCOUNT_TO_SHADOWBOXES_DESC = "在所有的 Shadowbox 上取消分配 Key";

    // 账户任务

    String ALLOCATE_SHADOWBOX_TO_ACCOUNTS = "TASK2001";
    String ALLOCATE_SHADOWBOX_TO_ACCOUNTS_DESC = "为所有的 Account 分配 Key";

    String SAVE_PULL_HISTORY_LOCATION = "TASK2004";
    String SAVE_PULL_HISTORY_LOCATION_DESC = "获取订阅拉取记录的地理位置";

    String PULL_HISTORY_NOTICE = "TASK2005";
    String PULL_HISTORY_NOTICE_DESC = "订阅拉取记录通知";

    // 订阅任务

    String GENERATE_SUBSCRIBE_AND_SHORT_URL = "TASK3001";
    String GENERATE_SUBSCRIBE_AND_SHORT_URL_DESC = "生成订阅链接和短链接 URL";

    // 通知任务

    String NOTICE_ACCOUNT_CREATED = "NOTICE1001";
    String NOTICE_ACCOUNT_CREATED_DESC = "通知用户账户已经创建";

    String NOTICE_ACCOUNT_ALLOCATE_NEW_KEY = "NOTICE1002";
    String NOTICE_ACCOUNT_ALLOCATE_NEW_KEY_DESC = "通知所有用户已经分配了新的 Key";

    String NOTICE_ACCOUNT_TRAFFIC_WARNING = "NOTICE1003";
    String NOTICE_ACCOUNT_TRAFFIC_WARNING_DESC = "通知用户流量即将用尽";

    String NOTICE_ACCOUNT_EXPIRED = "NOTICE1004";
    String NOTICE_ACCOUNT_EXPIRED_DESC = "通知用户账户已过期";

    String NOTICE_ACCOUNT_TRAFFIC_OVERED = "NOTICE1005";
    String NOTICE_ACCOUNT_TRAFFIC_OVERED_DESC = "通知用户流量已经用尽";

    String NOTICE_ACCOUNT_TRAFFIC_RESET = "NOTICE1006";
    String NOTICE_ACCOUNT_TRAFFIC_RESET_DESC = "通知用户流量已重置";
}
