package cn.azhicloud.olserv.repository.mapper;

import java.util.List;

import cn.azhicloud.olserv.model.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/13 13:30
 */
@Mapper
public interface AccessLogMapper {

    @Select("SELECT * FROM access_log WHERE DATE_SUB(CURDATE(),INTERVAL #{days} DAY) > created")
    List<AccessLog> selectCreatedBeforeDays(Integer days);
}
