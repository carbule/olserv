package cn.azhicloud.sequence;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.azhicloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Sequence 工具类
 *
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 14:46
 */
@Component
@Slf4j
public class Sequences {

    private static final Queue<Long> QUEUE = new LinkedBlockingQueue<>(100);

    private static SequenceService sequenceService;

    public static Long next() {
        return Optional.ofNullable(QUEUE.poll()).orElseGet(() -> {
            fillQueue(sequenceService.nextSync());
            return QUEUE.poll();
        });
    }

    private static void fillQueue(Long next) {
        while (QUEUE.offer(next)) {
            fillQueue(sequenceService.nextSync());
        }
    }

    public Sequences(SequenceService sequenceService) {
        Sequences.sequenceService = sequenceService;
    }
}
