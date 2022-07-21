package cn.azhicloud.olserv.model.entity;

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

    @Id
    private String apiUrl;

    private String name;

    private String serverId;

    private Boolean metricsEnabled;

    private Long createdTimestampMs;

    private Integer portForNewAccessKeys;

    @Transient
    private List<AccessKey> accessKeys = Collections.emptyList();
}
