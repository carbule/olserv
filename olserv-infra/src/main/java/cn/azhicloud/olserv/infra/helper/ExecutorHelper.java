package cn.azhicloud.olserv.infra.helper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 14:30
 */
public class ExecutorHelper {

    /**
     * 创建和集合大小相同的线程池，异步执行
     *
     * @param elements 业务元素
     * @param consumer 消费者
     * @param <T>      业务对象
     */
    public static <T> void execute(List<T> elements, Consumer<T> consumer) {
        CountDownLatch latch = new CountDownLatch(elements.size());
        ExecutorService executor = Executors.newCachedThreadPool();

        for (T t : elements) {
            executor.execute(() -> {
                consumer.accept(t);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
