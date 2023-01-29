package cn.azhicloud.olserv.audit.domain;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:03
 */
@Data
public class AuditMessage {

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

    private LocalDateTime time;
}
