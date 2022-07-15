package cn.azhicloud.task.repository;

import cn.azhicloud.task.model.entity.AutoTaskCfg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 17:52
 */
public interface AutoTaskCfgRepository extends JpaRepository<AutoTaskCfg, String> {

    AutoTaskCfg findByTaskType(String type);
}
