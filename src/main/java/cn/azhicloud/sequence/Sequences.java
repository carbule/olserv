package cn.azhicloud.sequence;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import cn.azhicloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
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

    private static final LinkedBlockingQueue<Long> QUEUE = new LinkedBlockingQueue<>(1000);

    private static SequenceService sequenceService;

    public static Long next() {
        return Optional.ofNullable(QUEUE.poll()).orElseGet(() -> {
            log.warn(">>> Sequence queue is empty, start manual fill queue.");
            fillQueue(sequenceService.next());
            return QUEUE.poll();
        });
    }

    private static void fillQueue(Long next) {
        while (QUEUE.offer(next)) {
            fillQueue(sequenceService.next());
        }
    }

    public Sequences(SequenceService sequenceService) {
        Sequences.sequenceService = sequenceService;
        // 启动单线程异步实时填充队列
        Executors.newSingleThreadExecutor().execute(this::asyncFillQueue);
    }

    public void asyncFillQueue() {
        while (true) {
            try {
                QUEUE.put(sequenceService.next());
            } catch (InterruptedException e) {
                log.error(">>> Fill queue failed.", e);
            }
        }
    }
}
