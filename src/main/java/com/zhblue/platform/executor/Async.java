package com.zhblue.platform.executor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.zhblue.platform.callback.DefaultGroupCallback;
import com.zhblue.platform.callback.IGroupCallback;
import com.zhblue.platform.wrapper.WorkerWrapper;

import lombok.NonNull;

/**
 * @author ZHBlue
 * @since 2022/5/26 15:58
 */
public class Async {

    /**
     * 默认不定长线程池
     */
    private static final ThreadPoolExecutor COMMON_POOL =
        new ThreadPoolExecutor(20, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    /**
     * 注意，这里是个static，也就是只能有一个线程池。用户自定义线程池时，也只能定义一个
     */
    private static ExecutorService executorService;

    /**
     * 
     * @param timeout
     *            超时时长，毫秒
     * @param executorService
     *            线程池服务
     * @param workerWrappers
     *            Wrapper
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean beginWork(@NonNull long timeout, @NonNull ExecutorService executorService,
        List<WorkerWrapper> workerWrappers) throws ExecutionException, InterruptedException {
        if (workerWrappers == null || workerWrappers.size() == 0) {
            return false;
        }
        // 保存线程池变量
        // Async.executorService = executorService;
        // 定义一个map，存放所有的wrapper，key为wrapper的唯一id，value是该wrapper，可以从value中获取wrapper的result
        Map<String, WorkerWrapper> forParamUseWrappers = new ConcurrentHashMap<>();
        CompletableFuture[] futures = new CompletableFuture[workerWrappers.size()];
        for (int i = 0; i < workerWrappers.size(); i++) {
            WorkerWrapper wrapper = workerWrappers.get(i);
            futures[i] = CompletableFuture.runAsync(() -> wrapper.work(executorService, timeout, forParamUseWrappers),
                executorService);
        }
        try {
            CompletableFuture.allOf(futures).get(timeout, TimeUnit.MILLISECONDS);
            return true;
        } catch (TimeoutException e) {
            Set<WorkerWrapper> set = new HashSet<>();
            totalWorkers(workerWrappers, set);
            for (WorkerWrapper wrapper : set) {
                wrapper.stopNow();
            }
            return false;
        }
    }

    /**
     * 提交任务
     * 
     * @param executorService
     *            线程池
     * @param workerWrappers
     *            Wrappers
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean beginWork(@NonNull ExecutorService executorService, List<WorkerWrapper> workerWrappers)
        throws ExecutionException, InterruptedException {
        if (workerWrappers == null || workerWrappers.size() == 0) {
            return false;
        }
        Map<String, WorkerWrapper> forParamUseWrappers = new ConcurrentHashMap<>();
        CompletableFuture[] futures = new CompletableFuture[workerWrappers.size()];
        for (int i = 0; i < workerWrappers.size(); i++) {
            WorkerWrapper wrapper = workerWrappers.get(i);
            futures[i] =
                CompletableFuture.runAsync(() -> wrapper.work(executorService, forParamUseWrappers), executorService);
        }

        CompletableFuture.allOf(futures).get();
        return true;

    }

    /**
     * 自定义线程池，请传pool。不自定义的话，就走默认的COMMON_POOL
     * 
     * @param timeout
     *            超时时长，毫秒
     * 
     * @param executorService
     *            线程池服务
     * @param workerWrapper
     *            Wrapper
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean beginWork(long timeout, @NonNull ExecutorService executorService,
        WorkerWrapper... workerWrapper) throws ExecutionException, InterruptedException {
        if (workerWrapper == null || workerWrapper.length == 0) {
            return false;
        }
        List<WorkerWrapper> workerWrappers = Arrays.stream(workerWrapper).collect(Collectors.toList());
        return beginWork(timeout, executorService, workerWrappers);
    }

    /**
     * 自定义线程池，请传pool。不自定义的话，就走默认的COMMON_POOL
     * 
     * @param executorService
     *            线程池服务
     * @param workerWrapper
     *            Wrapper
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean beginWork(@NonNull ExecutorService executorService, WorkerWrapper... workerWrapper)
        throws ExecutionException, InterruptedException {
        if (workerWrapper == null || workerWrapper.length == 0) {
            return false;
        }
        List<WorkerWrapper> workerWrappers = Arrays.stream(workerWrapper).collect(Collectors.toList());
        return beginWork(executorService, workerWrappers);
    }

    /**
     * 同步阻塞,直到所有都完成,或失败
     * 
     * @param timeout
     *            超时时长
     * @param workerWrapper
     *            Wrapper
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean beginWork(@NonNull long timeout, WorkerWrapper... workerWrapper)
        throws ExecutionException, InterruptedException {
        return beginWork(timeout, COMMON_POOL, workerWrapper);
    }

    /**
     * 
     * @param workerWrapper
     *            Wrapper
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean beginWork(WorkerWrapper... workerWrapper) throws ExecutionException, InterruptedException {
        return beginWork(COMMON_POOL, workerWrapper);
    }

    /**
     * 异步执行：直到所有都完成,或失败后，发起回调
     * 
     * @param timeout
     *            超时时长
     * @param groupCallback
     *            回调
     * @param workerWrapper
     *            Wrapper
     */
    public static void beginWorkAsync(long timeout, IGroupCallback groupCallback, WorkerWrapper... workerWrapper) {
        beginWorkAsync(timeout, COMMON_POOL, groupCallback, workerWrapper);
    }

    /**
     * 异步执行：直到所有都完成,或失败后，发起回调
     * 
     * @param groupCallback
     * @param workerWrapper
     */
    public static void beginWorkAsync(IGroupCallback groupCallback, WorkerWrapper... workerWrapper) {
        beginWorkAsync(COMMON_POOL, groupCallback, workerWrapper);
    }

    /**
     * 异步执行,直到所有都完成,或失败后，发起回调
     * 
     * @param timeout
     *            超时时长 毫秒
     * @param executorService
     *            线程池
     * @param groupCallback
     *            统一回调处理类
     * @param workerWrapper
     *            Wrapper
     */
    public static void beginWorkAsync(long timeout, @NonNull ExecutorService executorService,
        IGroupCallback groupCallback, WorkerWrapper... workerWrapper) {
        if (groupCallback == null) {
            groupCallback = new DefaultGroupCallback();
        }
        IGroupCallback finalGroupCallback = groupCallback;
        executorService.submit(() -> {
            try {
                boolean success = beginWork(timeout, executorService, workerWrapper);
                if (success) {
                    finalGroupCallback.success(Arrays.asList(workerWrapper));
                } else {
                    finalGroupCallback.failure(Arrays.asList(workerWrapper), new TimeoutException());
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                finalGroupCallback.failure(Arrays.asList(workerWrapper), e);
            }
        });

    }

    /**
     * 异步执行,直到所有都完成,或失败后，发起回调
     *
     * @param executorService
     *            线程池
     * @param groupCallback
     *            统一回调处理类
     * @param workerWrapper
     *            Wrapper
     */
    public static void beginWorkAsync(@NonNull ExecutorService executorService, IGroupCallback groupCallback,
        WorkerWrapper... workerWrapper) {
        if (groupCallback == null) {
            groupCallback = new DefaultGroupCallback();
        }
        IGroupCallback finalGroupCallback = groupCallback;
        executorService.submit(() -> {
            try {
                boolean success = beginWork(executorService, workerWrapper);
                if (success) {
                    finalGroupCallback.success(Arrays.asList(workerWrapper));
                } else {
                    finalGroupCallback.failure(Arrays.asList(workerWrapper), new TimeoutException());
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                finalGroupCallback.failure(Arrays.asList(workerWrapper), e);
            }
        });

    }

    /**
     * 总共多少个执行单元
     */
    @SuppressWarnings("unchecked")
    private static void totalWorkers(List<WorkerWrapper> workerWrappers, Set<WorkerWrapper> set) {
        set.addAll(workerWrappers);
        for (WorkerWrapper wrapper : workerWrappers) {
            if (wrapper.getNextWrappers() == null) {
                continue;
            }
            List<WorkerWrapper> wrappers = wrapper.getNextWrappers();
            totalWorkers(wrappers, set);
        }

    }

    /**
     * 关闭线程池
     */
    public static void shutDown() {
        shutDown(executorService);
    }

    /**
     * 关闭线程池
     */
    public static void shutDown(ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdown();
        } else {
            COMMON_POOL.shutdown();
        }
    }

    public static String getThreadCount() {
        return "activeCount=" + COMMON_POOL.getActiveCount() + "  completedCount " + COMMON_POOL.getCompletedTaskCount()
            + "  largestCount " + COMMON_POOL.getLargestPoolSize();
    }
}
