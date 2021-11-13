package cn.azhicloud.olserv.service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/5 21:19
 */
public interface SchedulerService {

    /**
     * 持久化 access-keys
     */
    void persistenceAccessKeys();

    /**
     * 访问统计
     */
    void accessStatistics();
}
