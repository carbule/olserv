package cn.azhicloud.olserv.repository.mapper;

import java.time.LocalDateTime;
import java.util.List;

import cn.azhicloud.olserv.model.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/17 13:11
 */
@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account WHERE created_at <= #{dateTime}")
    List<Account> selectAccountsBeforeDate(LocalDateTime dateTime);

    @Select("SELECT * FROM account WHERE expired_at < NOW()")
    List<Account> selectExpiredAccounts();
}
