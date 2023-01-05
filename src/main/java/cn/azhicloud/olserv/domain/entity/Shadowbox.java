package cn.azhicloud.olserv.domain.entity;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.Application;
import cn.azhicloud.olserv.domain.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
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
     * 是否离线
     */
    private Boolean offline;

    /**
     * 该服务器下拥有的 Keys
     */
    @Transient
    private List<AccessKey> accessKeys = Collections.emptyList();

    public URI URI() {
        return URI.create(apiUrl);
    }

    public void setAccessKey(AccessKey accessKey) {
        if (accessKey != null) {
            this.accessKeys = Collections.singletonList(accessKey);
        }
    }

    public static ShadowboxRepository repository() {
        return Application.getBean(ShadowboxRepository.class);
    }

    public static Shadowbox of(String id) {
        return repository().findById(id)
                .orElseThrow(() -> BizException.format("%s(%s) 实体不存在",
                        Shadowbox.class.getSimpleName(), id));
    }
}
