package cn.azhicloud.olserv.task.service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 20:05
 */
public interface AutoTaskBaseService {

    /**
     * 创建任务
     *
     * @param taskType 任务类型
     * @param taskData 数据
     * @return taskNo
     */
    String createAutoTask(String taskType, String taskData);

    /**
     * 创建任务并发布 MQ
     *
     * @param taskType 任务类型
     * @param taskData 数据
     * @return taskNo
     */
    String createAutoTaskAndPublicMQ(String taskType, String taskData);
}
