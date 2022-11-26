package cn.azhicloud.olserv.domain.model.outline;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:51
 */
@Data
public class Server {

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 服务器信息
     */
    private String serverId;

    /**
     * 服务器信息
     */
    private Boolean metricsEnabled;

    /**
     * 服务器信息
     */
    private Long createdTimestampMs;

    /**
     * 新增 Key 端口号
     */
    private Integer portForNewAccessKeys;
}
