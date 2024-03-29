package cn.azhicloud.olserv.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.Application;
import cn.azhicloud.olserv.repository.ServerOfflineRepository;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 13:54
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ServerOffline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 服务器唯一标识 {@link Shadowbox#getApiUrl()}
     */
    private String serverId;

    /**
     * 离线持续时间
     */
    private Integer offlineDurations;

    /**
     * 持续时间单位
     */
    private String durationTimeunit;

    public static ServerOfflineRepository repository() {
        return Application.getBean(ServerOfflineRepository.class);
    }

    public static ServerOffline of(Long id) {
        return repository().findById(id).orElseThrow(() ->
                BizException.format("%s(%s) 实体不存在", ServerOffline.class.getSimpleName(), id));
    }
}
