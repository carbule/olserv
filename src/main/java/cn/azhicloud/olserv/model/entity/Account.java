package cn.azhicloud.olserv.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:33
 */
@Data
@Entity
public class Account {

    @Id
    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private String username;

    private LocalDateTime lastAccess;

    @Transient
    private String subscribe;
}
