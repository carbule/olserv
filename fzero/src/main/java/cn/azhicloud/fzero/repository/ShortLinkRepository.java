package cn.azhicloud.fzero.repository;

import cn.azhicloud.fzero.model.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 00:21
 */
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    /**
     * select * from where code = ?
     *
     * @param code 唯一编码
     * @return entity
     */
    ShortLink findByCode(String code);
}
