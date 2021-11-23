package cn.azhicloud.sequence.repository;

import cn.azhicloud.sequence.entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 14:36
 */
public interface SequenceRepository extends JpaRepository<Sequence, Long> {
}
