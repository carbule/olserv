package cn.azhicloud.olserv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:34
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}
