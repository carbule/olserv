package cn.azhicloud.olserv.model.entity;

import javax.persistence.Entity;

import cn.azhicloud.olserv.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Account extends BaseEntity {

    private String username;
}
