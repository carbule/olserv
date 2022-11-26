package cn.azhicloud.olserv.repository.mapper;

import java.time.LocalDateTime;
import java.util.List;

import cn.azhicloud.olserv.domain.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/17 13:11
 */
@Mapper
public interface AccountMapper {

    /**
     * 查询创建时间在 dateTime 前的账户
     *
     * @param dateTime 时间
     * @return ls
     */
    @Select("SELECT * FROM account WHERE created_at <= #{dateTime}")
    List<Account> selectAccountsBeforeDate(LocalDateTime dateTime);

    /**
     * 查询过期的账户
     *
     * @return ls
     */
    @Select("SELECT * FROM account WHERE expired_at < NOW()")
    List<Account> selectExpiredAccounts();
}
