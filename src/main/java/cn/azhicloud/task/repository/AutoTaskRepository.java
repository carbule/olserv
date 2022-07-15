package cn.azhicloud.task.repository;

import java.util.List;

import cn.azhicloud.task.model.entity.AutoTask;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 17:53
 */
public interface AutoTaskRepository extends JpaRepository<AutoTask, Long> {

    AutoTask findByTaskNo(String taskNo);

    AutoTask findByTaskNoAndStatus(String taskNo, String status);

    List<AutoTask> findByStatus(String status);
}
