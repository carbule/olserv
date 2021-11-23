package cn.azhicloud.olserv.repository;

import cn.azhicloud.olserv.BaseEntity;
import cn.azhicloud.olserv.BaseRepository;
import cn.azhicloud.olserv.model.entity.AccessLog;
import cn.azhicloud.sequence.Sequences;

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
        AccessLog log = BaseEntity.instance(AccessLog.class);
        log.setId(Sequences.next());
        log.setUsername(username);
        log.setReturnContent(returnContent);

        save(log);
    }
}
