package cn.azhicloud.olserv.repository;

import cn.azhicloud.olserv.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:35
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByUsername(String username);
}