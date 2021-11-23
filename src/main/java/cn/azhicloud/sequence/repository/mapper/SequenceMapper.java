package cn.azhicloud.sequence.repository.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 14:37
 */
@Mapper
public interface SequenceMapper {

    /**
     * 插入新号
     */
    @Insert("INSERT INTO sequence(id) SELECT MAX(id) + CEIL(RAND() * 5) FROM sequence")
    void insert();

    /**
     * 获取新号
     *
     * @return id
     */
    @Select("SELECT MAX(id) FROM sequence")
    Long select();
}
