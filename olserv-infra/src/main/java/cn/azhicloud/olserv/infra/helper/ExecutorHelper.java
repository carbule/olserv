package cn.azhicloud.olserv.infra.helper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

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

    /**
     * 创建和集合大小相同的线程池，异步执行
     *
     * @param elements 业务元素
     * @param function 业务代码
     * @param <T>      业务对象
     * @return 执行结果
     */
    public static <T, R> List<R> execute(List<T> elements, Function<T, R> function) {
        CountDownLatch latch = new CountDownLatch(elements.size());
        ExecutorService executor = Executors.newCachedThreadPool();

        List<R> results = new CopyOnWriteArrayList<>();
        for (T t : elements) {
            executor.execute(() -> {
                results.add(function.apply(t));
                latch.countDown();
            });
        }
        try {
            latch.await();
            return results;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
