package cn.azhicloud.olserv.model.outline;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:51
 */
@Data
public class ServerInformation implements Serializable {

    private static final long serialVersionUID = -1586262852471556811L;

    /**
     * 服务器名称
     */
    private String name;

    private String serverId;

    private Boolean metricsEnabled;

    private Long createdTimestampMs;

    private String version;

    /**
     * 新建 access-key 访问端口号
     */
    private Integer portForNewAccessKeys;

    /**
     * 新建 access-key 访问地址
     */
    private String hostnameForAccessKeys;
}
