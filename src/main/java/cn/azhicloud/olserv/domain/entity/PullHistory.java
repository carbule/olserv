package cn.azhicloud.olserv.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.Application;
import cn.azhicloud.olserv.repository.PullHistoryRepository;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/11/3 15:24
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PullHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账号 ID
     */
    private String accountId;

    /**
     * 账号用户名
     */
    private String accountUsername;

    /**
     * 访问 IP 地址
     */
    private String fromIp;

    /**
     * 访问 IP 归属地
     */
    private String fromLocation;

    /**
     * 拉取内容
     */
    @Column(columnDefinition = "text")
    private String pullContent;

    /**
     * 邮件通知状态
     */
    private String notifyStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    public static PullHistoryRepository repository() {
        return Application.getBean(PullHistoryRepository.class);
    }

    public static PullHistory of(Long id) {
        return repository().findById(id).orElseThrow(() ->
                BizException.format("PullHistory(%s) 实体不存在", id));
    }
}
