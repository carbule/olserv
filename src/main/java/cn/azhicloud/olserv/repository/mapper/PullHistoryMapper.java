package cn.azhicloud.olserv.repository.mapper;

import java.util.List;

import cn.azhicloud.olserv.domain.entity.PullHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/11/26 11:34
 */
@Mapper
public interface PullHistoryMapper {

    /**
     * 查询账户一分钟内的拉取历史
     *
     * @param accountId 账户 ID
     * @return 历史列表
     */
    @Select("SELECT * FROM pull_history " +
            "WHERE created_at >= DATE_SUB(NOW(), INTERVAL 1 MINUTE)" +
            "AND account_id = #{accountId}")
    List<PullHistory> selectHistoriesWithinOneMin(@Param("accountId") String accountId);
}
