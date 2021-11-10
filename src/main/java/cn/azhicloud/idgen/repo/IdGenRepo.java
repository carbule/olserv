package cn.azhicloud.idgen.repo;

import cn.azhicloud.idgen.model.entity.IdGen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 17:48
 */
@Repository
public interface IdGenRepo extends JpaRepository<IdGen, Long> {
}
