package cn.azhicloud.olserv.model.entity;

import javax.persistence.Entity;

import cn.azhicloud.olserv.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/5 21:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AccessKey extends BaseEntity {

    private String serverName;

    private String keyId;

    private String name;

    private String password;

    private Integer port;

    private String method;

    private String accessUrl;
}
