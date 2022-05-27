package com.zhblue.platform.callback;

import java.util.List;

import com.zhblue.platform.wrapper.WorkerWrapper;

/**
 * 群组默认回调
 * 
 * @author ZHBlue
 * @since 2022/5/26 15:58
 */
public class DefaultGroupCallback implements IGroupCallback {
    /**
     * 成功后，可以从wrapper里去getWorkResult
     *
     * @param workerWrappers
     */
    @Override
    public void success(List<WorkerWrapper> workerWrappers) {

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
