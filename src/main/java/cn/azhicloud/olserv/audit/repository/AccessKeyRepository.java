package cn.azhicloud.olserv.audit.repository;

import cn.azhicloud.olserv.audit.domain.entity.AccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:09
 */
public interface AccessKeyRepository extends JpaRepository<AccessKey, Long> {
}
