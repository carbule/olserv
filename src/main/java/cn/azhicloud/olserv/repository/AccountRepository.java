package cn.azhicloud.olserv.repository;

import java.util.List;

import cn.azhicloud.olserv.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:35
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByUsername(String username);

    List<Account> findByEmailNotNull();

    Account findByUsernameOrEmail(String username, String email);
}
