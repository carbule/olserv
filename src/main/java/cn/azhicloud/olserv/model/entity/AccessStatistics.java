package cn.azhicloud.olserv.model.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;

import cn.azhicloud.olserv.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/11/13 14:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AccessStatistics extends BaseEntity {

    @Column(unique = true)
    private String username;

    private Integer count;

    private Date lastAccess;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    public String getLastAccessStr() {
        return lastAccess == null ? null :
                ZonedDateTime.ofInstant(lastAccess.toInstant(), ZONE_ID).format(FORMATTER);
    }
}
