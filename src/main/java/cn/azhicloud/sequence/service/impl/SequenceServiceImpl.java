package cn.azhicloud.sequence.service.impl;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import cn.azhicloud.sequence.entity.Sequence;
import cn.azhicloud.sequence.repository.SequenceRepository;
import cn.azhicloud.sequence.repository.mapper.SequenceMapper;
import cn.azhicloud.sequence.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 14:45
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SequenceServiceImpl implements SequenceService, CommandLineRunner {

    @Value("${sequence.init}")
    private Long initSequence;

    private final SequenceMapper sequenceMapper;

    private final SequenceRepository sequenceRepository;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public synchronized Long nextSync() {
        sequenceMapper.insert();
        return sequenceMapper.select();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Long next() {
        sequenceMapper.update();
        return sequenceMapper.select();
    }

    @Override
    public void run(String... args) throws Exception {
        if (sequenceRepository.count() == 0) {
            sequenceRepository.save(new Sequence(initSequence));
        }
    }
}
