package cn.azhicloud.olserv;

import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import cn.azhicloud.idgen.util.IdGenUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:33
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    private Long id;

    @CreatedDate
    private Date created;

    public BaseEntity() {
        this.id = IdGenUtils.genId();
    }

    private void setId(Long id) {
        this.id = id;
    }
}
