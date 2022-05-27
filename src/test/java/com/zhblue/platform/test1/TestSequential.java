package com.zhblue.platform.test1;

import com.zhblue.platform.executor.Async;
import com.zhblue.platform.utils.SystemClock;
import com.zhblue.platform.wrapper.WorkerWrapper;

import java.util.concurrent.ExecutionException;

/**
 * @author ZHBlue
 * @since 2022/5/26 16:49
 */
public class TestSequential {

    public static void main(String[] args) throws InterruptedException, ExecutionException {


        SeqWorker w = new SeqWorker();
        SeqWorker1 w1 = new SeqWorker1();
        SeqWorker2 w2 = new SeqWorker2();

        //顺序0-1-2
        WorkerWrapper<String, String> workerWrapper2 =  new WorkerWrapper.Builder<String, String>()
                .worker(w2)
                .callback(w2)
                .param("2")
                .build();

        WorkerWrapper<String, String> workerWrapper1 =  new WorkerWrapper.Builder<String, String>()
                .worker(w1)
                .callback(w1)
                .param("1")
                .next(workerWrapper2)
                .build();

        WorkerWrapper<String, String> workerWrapper =  new WorkerWrapper.Builder<String, String>()
                .worker(w)
                .callback(w)
                .param("0")
                .next(workerWrapper1)
                .build();

//        testNormal(workerWrapper);

        testGroupTimeout(workerWrapper);
    }

    private static void testGroupTimeout(WorkerWrapper<String, String> workerWrapper) throws ExecutionException, InterruptedException {
        long now = SystemClock.now();
        System.out.println("begin-" + now);

        Async.beginWork(4500, workerWrapper);

        System.out.println("end-" + SystemClock.now());
        System.err.println("cost-" + (SystemClock.now() - now));
//        Async.shutDown();
    }
}
