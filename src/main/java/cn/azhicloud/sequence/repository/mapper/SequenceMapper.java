package cn.azhicloud.sequence.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 14:37
 */
@Mapper
public interface SequenceMapper {

    /**
     * 更新新号
     */
    @Update("UPDATE sequence SET id = (SELECT MAX(id) + CEIL(RAND() * 5)) ORDER BY id DESC LIMIT 1")
    void update();

    /**
     * 获取新号
     *
     * @return id
     */
    @Select("SELECT MAX(id) FROM sequence")
    Long select();
}
