package cn.azhicloud.olserv.model.entity;

import javax.persistence.Entity;

import cn.azhicloud.olserv.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Where(clause = "enabled = 1")
public class Shadowbox extends BaseEntity {

    private String name;

    private String apiUrl;

    private String certSha256;

    private Boolean enabled;

    private String redirectAddress;

    private Integer redirectPort;
}
