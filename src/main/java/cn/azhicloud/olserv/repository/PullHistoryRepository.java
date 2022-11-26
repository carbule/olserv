package cn.azhicloud.olserv.repository;

import cn.azhicloud.olserv.domain.entity.PullHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/11/26 10:38
 */
public interface PullHistoryRepository extends JpaRepository<PullHistory, Long> {
}
