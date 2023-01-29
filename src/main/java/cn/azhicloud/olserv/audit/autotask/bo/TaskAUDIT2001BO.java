package cn.azhicloud.olserv.audit.autotask.bo;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:51
 */
@Data
public class TaskAUDIT2001BO {

    /**
     * 客户端地址
     */
    private String clientAddr;

    /**
     * access key 访问密钥
     */
    private String secret;

    /**
     * 访问目标地址
     */
    private String target;

    /**
     * 触发时间
     */
    private LocalDateTime time;
}
