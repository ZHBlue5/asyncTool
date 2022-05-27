package com.zhblue.platform.test2;

import java.util.List;

import com.zhblue.platform.callback.IGroupCallback;
import com.zhblue.platform.wrapper.WorkerWrapper;

/**
 * @author ZHBlue
 * @since 2022/5/27 10:37
 */
public class GroupCallback implements IGroupCallback {
    /**
     * 成功后，可以从wrapper里去getWorkResult
     *
     * @param workerWrappers
     */
    @Override
    public void success(List<WorkerWrapper> workerWrappers) {
        System.out.println("成功：" + workerWrappers.size());
    }

    /**
     * 失败了，也可以从wrapper里去getWorkResult
     *
     * @param workerWrappers
     * @param e
     */
    @Override
    public void failure(List<WorkerWrapper> workerWrappers, Exception e) {

    }
}
