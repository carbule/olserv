package cn.azhicloud.olserv.repository;

import java.util.List;

import cn.azhicloud.olserv.BaseRepository;
import cn.azhicloud.olserv.model.entity.AccessKey;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/5 21:14
 */
public interface AccessKeyRepos extends BaseRepository<AccessKey> {

    /**
     * 根据名称查询 access-keys
     *
     * @param name 账号名
     * @return List
     */
    List<AccessKey> findByName(String name);
}
