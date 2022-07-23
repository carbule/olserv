package cn.azhicloud.olserv.task.service;

/**
 * 消息队列和调度触发执行自动任务业务代码
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 18:00
 */
public interface AutoTaskBizService {

    /**
     * 执行任务
     *
     * @param taskNo   任务编号
     * @param executor 执行人
     */
    void execute(String taskNo, String executor);

    /**
     * 执行任务（调度系统入口）
     */
    void executeByJob();
}
