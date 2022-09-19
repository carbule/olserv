package cn.azhicloud.olserv.repository;

import java.util.List;
import java.util.Set;

import cn.azhicloud.olserv.model.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 22:28
 */
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    /**
     * 根据账户 ID 查询
     *
     * @param ids 账户 ID
     * @return ls
     */
    List<Subscribe> findByAccountIdIn(Set<String> ids);

    /**
     * 根据账户 ID 查询
     *
     * @param accountId 账户 ID
     * @return ls
     */
    Subscribe findByAccountId(String accountId);
}
