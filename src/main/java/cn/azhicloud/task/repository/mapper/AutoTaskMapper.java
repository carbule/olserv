package cn.azhicloud.task.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 22:27
 */
@Mapper
public interface AutoTaskMapper {

    /**
     * 查询 days 天前完成的自动任务 ID
     *
     * @param days 天
     * @return ls
     */
    @Select("SELECT task_id FROM auto_task " +
            "WHERE `status` = '90' " +
            "AND created_at < DATE_SUB(NOW(), INTERVAL #{days} DAY)")
    List<Long> selectFinishedTaskIdsBeforeDays(int days);
}
