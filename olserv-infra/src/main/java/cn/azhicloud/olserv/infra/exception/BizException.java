package cn.azhicloud.olserv.infra.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/14 17:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -9002390906318579793L;

    /**
     * 10：业务系统 | 0000：异常编号，递增
     */
    private static final String DEFAULT_BIZ_CODE = "100000";

    /**
     * 业务码
     */
    private String bizCode;

    /**
     * 业务信息
     */
    private String bizMessage;

    public BizException(String bizMessage) {
        super(bizMessage);
        this.bizCode = DEFAULT_BIZ_CODE;
        this.bizMessage = bizMessage;
    }

    public BizException(String bizCode, String bizMessage) {
        super(bizMessage);
        this.bizCode = bizCode;
        this.bizMessage = bizMessage;
    }

    /**
     * string.format
     *
     * @param bizMessage 业务信息
     * @param args       参数
     * @return BizException
     */
    public static BizException format(String bizMessage, Object... args) {
        return new BizException(String.format(bizMessage, args));
    }

    /**
     * string.format
     *
     * @param bizCode    业务码
     * @param bizMessage 业务信息
     * @param args       参数
     * @return BizException
     */
    public static BizException _format(String bizCode, String bizMessage, Object... args) {
        return new BizException(bizCode, String.format(bizMessage, args));
    }
}
