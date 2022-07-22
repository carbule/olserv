package cn.azhicloud.olserv.model.outline;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:51
 */
@Data
public class Server {

    private String name;

    private String serverId;

    private Boolean metricsEnabled;

    private Long createdTimestampMs;

    private Integer portForNewAccessKeys;
}
