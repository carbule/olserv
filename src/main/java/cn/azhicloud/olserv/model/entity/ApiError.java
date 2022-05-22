package cn.azhicloud.olserv.model.entity;

import cn.azhicloud.olserv.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/6 18:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ApiError extends BaseEntity {

    private String apiName;

    private String apiUrl;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private Date latest;
}
