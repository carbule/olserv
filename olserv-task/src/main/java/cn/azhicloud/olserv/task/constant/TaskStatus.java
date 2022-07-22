package cn.azhicloud.olserv.task.constant;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/9 12:42
 */
public enum TaskStatus {

    /**
     * 未处理
     */
    PENDING("00"),

    /**
     * 处理中
     */
    PROCESSING("10"),

    /**
     * 已完成
     */
    FINISHED("90"),

    /**
     * 失败
     */
    FAILED("91"),

    /**
     * 错误
     */
    ERROR("92");

    public final String value;

    TaskStatus(String value) {
        this.value = value;
    }
}
