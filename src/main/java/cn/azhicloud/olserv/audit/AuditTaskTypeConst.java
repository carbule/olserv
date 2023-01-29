package cn.azhicloud.olserv.audit;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:10
 */
public interface AuditTaskTypeConst {

    String PERSISTENT_ACCOUNT_OWNED_KEYS = "AUDIT1001";
    String PERSISTENT_ACCOUNT_OWNED_KEYS_DESC = "持久化用户拥有的 Key";

    String PROCESS_AUDIT_MESSAGE = "AUDIT2001";
    String PROCESS_AUDIT_MESSAGE_DESC = "处理来自outline-ss-server的审计数据";

    String SAVE_AUDIT_LOG_CLIENT_LOCATION = "AUDIT2002";
    String SAVE_AUDIT_LOG_CLIENT_LOCATION_DESC = "保存审计日志客户端IP归属地";
}
