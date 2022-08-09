package cn.azhicloud.olserv.infra.helper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 14:30
 */
@Slf4j
public class ExecutorHelper {

    /**
     * 任务执行失败默认重试次数
     */
    public static final int DEFAULT_RETRY_TIME = 0;

    /**
     * 任务执行所用线程池
     */
    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    /**
     * 单任务执行
     *
     * @param t         入参
     * @param consumer  实现
     * @param retryTime 重试次数
     * @param <T>       入参泛型
     */
    protected static <T> void execute(T t, Consumer<T> consumer, int retryTime) {
        int retry = 0;
        for (; ; ) {
            try {
                consumer.accept(t);
                // 执行成功跳出循环，如果失败执行重试
                return;
            } catch (Exception e) {
                if (++retry <= retryTime) {
                    log.error(e.getMessage());
                    log.error("execute failed, retry {} times", retry);
                    continue;
                }
                // 规定重试次数内依然失败，抛出异常
                throw e;
            }
        }
    }

    /**
     * 单任务执行
     *
     * @param t                 入参
     * @param consumer          实现
     * @param retryTime         重试次数
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参泛型
     */
    protected static <T> void execute(T t, Consumer<T> consumer, int retryTime,
                                      Consumer<Exception> exceptionConsumer) {
        try {
            execute(t, consumer, retryTime);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
        }
    }

    /**
     * 单任务执行
     *
     * @param t         入参 1
     * @param u         入参 2
     * @param consumer  实现
     * @param retryTime 重试次数
     * @param <T>       入参 1 泛型
     * @param <U>       入参 2 泛型
     */
    protected static <T, U> void execute(T t, U u, BiConsumer<T, U> consumer, int retryTime) {
        int retry = 0;
        for (; ; ) {
            try {
                consumer.accept(t, u);
                // 执行成功跳出循环，如果失败执行重试
                return;
            } catch (Exception e) {
                if (++retry < retryTime) {
                    log.error(e.getMessage());
                    log.error("execute failed, retry {} times", retry);
                    continue;
                }
                // 规定重试次数内依然失败，抛出异常
                throw e;
            }
        }
    }

    /**
     * 单任务执行
     *
     * @param t                 入参 1
     * @param u                 入参 2
     * @param consumer          实现
     * @param retryTime         重试次数
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参 1 泛型
     * @param <U>               入参 2 泛型
     */
    protected static <T, U> void execute(T t, U u, BiConsumer<T, U> consumer, int retryTime,
                                         Consumer<Exception> exceptionConsumer) {
        try {
            execute(t, u, consumer, retryTime);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
        }
    }

    /**
     * 单任务执行
     *
     * @param t         入参
     * @param function  实现
     * @param retryTime 重试次数
     * @param <T>       入参泛型
     * @param <R>       返回值泛型
     * @return 任务执行完成返回内容
     */
    protected static <T, R> R execute(T t, Function<T, R> function, int retryTime) {
        int retry = 0;
        for (; ; ) {
            try {
                return function.apply(t);
            } catch (Exception e) {
                if (++retry < retryTime) {
                    log.error(e.getMessage());
                    log.error("execute failed, retry {} times", retry);
                    continue;
                }
                // 规定重试次数内依然失败，抛出异常
                throw e;
            }
        }
    }

    /**
     * 单任务执行
     *
     * @param t                 入参
     * @param function          实现
     * @param retryTime         重试次数
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参泛型
     * @param <R>               返回值泛型
     * @return 任务执行完成返回内容
     */
    protected static <T, R> R execute(T t, Function<T, R> function, int retryTime,
                                      Consumer<Exception> exceptionConsumer) {
        try {
            return execute(t, function, retryTime);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return null;
        }
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT             入参
     * @param consumer          实现
     * @param retryTime         重试次数
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参泛型
     */
    public static <T> void execute(List<T> listT, Consumer<T> consumer, int retryTime,
                                   Consumer<Exception> exceptionConsumer) {
        CountDownLatch latch = new CountDownLatch(listT.size());

        for (T t : listT) {
            EXECUTOR.execute(() -> {
                try {
                    execute(t, consumer, retryTime, exceptionConsumer);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT             入参
     * @param consumer          实现
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参泛型
     */
    public static <T> void execute(List<T> listT, Consumer<T> consumer,
                                   Consumer<Exception> exceptionConsumer) {
        execute(listT, consumer, DEFAULT_RETRY_TIME, exceptionConsumer);
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT     入参
     * @param consumer  实现
     * @param retryTime 重试次数
     * @param <T>       入参泛型
     */
    public static <T> void execute(List<T> listT, Consumer<T> consumer, int retryTime) {
        Thread mainThread = Thread.currentThread();
        execute(listT, consumer, retryTime, ex -> {
            log.error(ex.getMessage(), ex);
            // 中断主线程
            mainThread.interrupt();
        });
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT    入参
     * @param consumer 实现
     * @param <T>      入参泛型
     */
    public static <T> void execute(List<T> listT, Consumer<T> consumer) {
        execute(listT, consumer, DEFAULT_RETRY_TIME);
    }


    /**
     * 多任务多线程拆分执行
     *
     * @param listT             入参 1
     * @param listU             入参 2
     * @param consumer          实现
     * @param retryTime         重试次数
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参 1 泛型
     * @param <U>               入参 2 泛型
     */
    public static <T, U> void execute(List<T> listT, List<U> listU, BiConsumer<T, U> consumer, int retryTime,
                                      Consumer<Exception> exceptionConsumer) {
        CountDownLatch latch = new CountDownLatch(listT.size() * listU.size());

        for (T t : listT) {
            EXECUTOR.execute(() -> {
                for (U u : listU) {
                    try {
                        execute(t, u, consumer, retryTime, exceptionConsumer);
                    } finally {
                        latch.countDown();
                    }
                }
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
     * 多任务多线程拆分执行
     *
     * @param listT             入参 1
     * @param listU             入参 2
     * @param consumer          实现
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参 1 泛型
     * @param <U>               入参 2 泛型
     */
    public static <T, U> void execute(List<T> listT, List<U> listU, BiConsumer<T, U> consumer,
                                      Consumer<Exception> exceptionConsumer) {
        execute(listT, listU, consumer, DEFAULT_RETRY_TIME, exceptionConsumer);
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT     入参 1
     * @param listU     入参 2
     * @param consumer  实现
     * @param retryTime 重试次数
     * @param <T>       入参 1 泛型
     * @param <U>       入参 2 泛型
     */
    public static <T, U> void execute(List<T> listT, List<U> listU, BiConsumer<T, U> consumer, int retryTime) {
        Thread mainThread = Thread.currentThread();
        execute(listT, listU, consumer, retryTime, ex -> {
            log.error(ex.getMessage(), ex);
            // 中断主线程
            mainThread.interrupt();
        });
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT    入参 1
     * @param listU    入参 2
     * @param consumer 实现
     * @param <T>      入参 1 泛型
     * @param <U>      入参 2 泛型
     */
    public static <T, U> void execute(List<T> listT, List<U> listU, BiConsumer<T, U> consumer) {
        execute(listT, listU, consumer, DEFAULT_RETRY_TIME);
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT             入参
     * @param function          实现
     * @param retryTime         重试次数
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参泛型
     * @param <R>               返回值泛型
     * @return 任务执行完成返回内容
     */
    public static <T, R> List<R> execute(List<T> listT, Function<T, R> function, int retryTime,
                                         Consumer<Exception> exceptionConsumer) {
        CountDownLatch latch = new CountDownLatch(listT.size());

        List<R> results = new CopyOnWriteArrayList<>();
        for (T t : listT) {
            EXECUTOR.execute(() -> {
                try {
                    results.add(execute(t, function, retryTime, exceptionConsumer));
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
            return results;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT             入参
     * @param function          实现
     * @param exceptionConsumer 重试失败后的异常处理
     * @param <T>               入参泛型
     * @param <R>               返回值泛型
     * @return 任务执行完成返回内容
     */
    public static <T, R> List<R> execute(List<T> listT, Function<T, R> function,
                                         Consumer<Exception> exceptionConsumer) {
        return execute(listT, function, DEFAULT_RETRY_TIME, exceptionConsumer);
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT     入参
     * @param function  实现
     * @param retryTime 重试次数
     * @param <T>       入参泛型
     * @param <R>       返回值泛型
     * @return 任务执行完成返回内容
     */
    public static <T, R> List<R> execute(List<T> listT, Function<T, R> function, int retryTime) {
        Thread mainThread = Thread.currentThread();
        return execute(listT, function, retryTime, ex -> {
            log.error(ex.getMessage(), ex);
            // 中断主线程
            mainThread.interrupt();
        });
    }

    /**
     * 多任务多线程拆分执行
     *
     * @param listT    入参
     * @param function 实现
     * @param <T>      入参泛型
     * @param <R>      返回值泛型
     * @return 任务执行完成返回内容
     */
    public static <T, R> List<R> execute(List<T> listT, Function<T, R> function) {
        return execute(listT, function, DEFAULT_RETRY_TIME);
    }
}
