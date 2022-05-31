package cn.azhicloud.olserv.repository;

import cn.azhicloud.olserv.model.entity.ApiErrorStats;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/26 17:14
 */
public interface ApiErrorStatsRepos extends JpaRepository<ApiErrorStats, String> {
}
