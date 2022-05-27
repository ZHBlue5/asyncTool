package com.zhblue.platform.test2;

import java.util.Map;

import com.zhblue.platform.callback.ICallback;
import com.zhblue.platform.callback.IWorker;
import com.zhblue.platform.utils.SystemClock;
import com.zhblue.platform.worker.WorkResult;
import com.zhblue.platform.wrapper.WorkerWrapper;

/**
 * @author wuweifeng wrote on 2019-11-20.
 */
public class ParWorker3 implements IWorker<String, String>, ICallback<String, String> {
    private long sleepTime = 1000;

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String action(String object, Map<String, WorkerWrapper> allWrappers) {
        try {
            System.out.println("获取2：" + allWrappers.get("2").getWorkResult().getResult());
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "result = " + SystemClock.now() + "---param = " + object + " from 3";
    }

    @Override
    public String defaultValue() {
        return "worker3--default";
    }

    @Override
    public void begin() {
        System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, WorkResult<String> workResult) {
        if (success) {
            System.out.println("callback worker3 success--" + SystemClock.now() + "----" + workResult.getResult()
                + "-threadName:" + Thread.currentThread().getName());
        } else {
            System.err.println("callback worker3 failure--" + SystemClock.now() + "----" + workResult.getResult()
                + "-threadName:" + Thread.currentThread().getName());
        }
    }

}
