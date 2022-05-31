package cn.azhicloud.olserv.repository.mapper;

import java.util.List;

import cn.azhicloud.olserv.model.entity.ApiError;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/12 17:16
 */
@Mapper
public interface ApiErrorMapper {

    @Select("SELECT * FROM api_error WHERE DATE_SUB(CURDATE(),INTERVAL #{days} DAY) > created")
    List<ApiError> selectCreatedBeforeDays(Integer days);

    @Select("SELECT * FROM api_error WHERE TO_DAYS(created) = TO_DAYS(NOW())")
    List<ApiError> selectCreatedAtToday();

    @Select("SELECT * FROM api_error WHERE TO_DAYS(created) = TO_DAYS(NOW()) - 1")
    List<ApiError> selectCreatedAtYesterday();
}
