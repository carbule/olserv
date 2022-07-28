package cn.azhicloud.olserv.repository.mapper;

import cn.azhicloud.olserv.model.entity.AccountTraffic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/27 14:17
 */
@Mapper
public interface AccountTrafficMapper {

    /**
     * 查询单个账户最新的流量统计
     *
     * @param accountId 账户 ID
     * @return entity
     */
    @Select("SELECT * FROM account_traffic WHERE account_id = #{accountId} " +
            "ORDER BY stats_date DESC LIMIT 1")
    AccountTraffic selectLatestAccountTrafficStats(@Param("accountId") String accountId);
}
