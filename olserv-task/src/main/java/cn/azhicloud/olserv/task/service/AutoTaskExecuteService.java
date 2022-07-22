package cn.azhicloud.olserv.task.service;

/**
 * 任务接口定义，所有任务实现都需要继承
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 17:57
 */
public interface AutoTaskExecuteService {

    /**
     * 执行
     *
     * @param taskData 业务数据
     */
    void execute(String taskData);
}
