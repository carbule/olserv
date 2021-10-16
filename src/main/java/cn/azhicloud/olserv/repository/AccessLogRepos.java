package cn.azhicloud.olserv.repository;

import cn.azhicloud.olserv.BaseRepository;
import cn.azhicloud.olserv.model.entity.AccessLog;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/16 11:04
 */
public interface AccessLogRepos extends BaseRepository<AccessLog> {

    /**
     * 新访问日志
     *
     * @param username      用户名
     * @param returnContent 返回内容
     */
    default void newAccessLog(String username, String returnContent) {
        AccessLog log = new AccessLog();
        log.setUsername(username);
        log.setReturnContent(returnContent);

        save(log);
    }
}
