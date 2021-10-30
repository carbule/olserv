package cn.azhicloud.idgen.model.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 17:46
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class IdGen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long seqKey;

    @CreatedDate
    private Date created;
}
