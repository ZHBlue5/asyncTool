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
public class ParWorker implements IWorker<String, String>, ICallback<String, String> {

    @Override
    public String action(String object, Map<String, WorkerWrapper> allWrappers) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "result = " + SystemClock.now() + "---param = " + object + " from 0";
    }

    @Override
    public String defaultValue() {
        return "worker0--default";
    }

    @Override
    public void begin() {
        System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, WorkResult<String> workResult) {
        if (success) {
            System.out.println("callback worker0 success--" + SystemClock.now() + "----" + workResult.getResult()
                + "-threadName:" + Thread.currentThread().getName());
        } else {
            System.err.println("callback worker0 failure--" + SystemClock.now() + "----" + workResult.getResult()
                + "-threadName:" + Thread.currentThread().getName());
        }
    }

}
