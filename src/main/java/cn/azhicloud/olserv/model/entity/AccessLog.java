package cn.azhicloud.olserv.model.entity;

import javax.persistence.Entity;

import cn.azhicloud.olserv.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/16 11:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AccessLog extends BaseEntity {

    private String username;

    private String returnContent;
}
