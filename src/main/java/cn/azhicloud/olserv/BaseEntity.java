package cn.azhicloud.olserv;

import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import cn.azhicloud.idgen.util.IdGenUtils;
import cn.azhicloud.sequence.Sequences;
import cn.azhicloud.sequence.entity.Sequence;
import com.fasterxml.jackson.databind.ser.Serializers;
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

    public static <T extends BaseEntity> T instance(Class<T> clazz) {
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        instance.setId(Sequences.next());
        return instance;
    }
}
