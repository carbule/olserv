package cn.azhicloud.olserv.model.entity;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import cn.azhicloud.olserv.model.outline.AccessKey;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:37
 */
@Data
@Entity
public class Shadowbox {

    /**
     * 服务器管理 API
     */
    @Id
    private String apiUrl;

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
     * Key 端口号
     */
    private Integer portForNewAccessKeys;

    /**
     * 该服务器下拥有的 Keys
     */
    @Transient
    private List<AccessKey> accessKeys = Collections.emptyList();

    public URI URI() {
        return URI.create(apiUrl);
    }
}
