package com.zhblue.platform.callback;

import java.util.List;

import com.zhblue.platform.wrapper.WorkerWrapper;

/**
 * 直到所有都完成,或失败后，发起回调
 * 
 * @author ZHBlue
 * @since 2022/5/26 15:34
 */
public interface IGroupCallback {
    /**
     * 成功后，可以从wrapper里去getWorkResult
     * 
     * @param workerWrappers
     */
    void success(List<WorkerWrapper> workerWrappers);

    /**
     * 失败了，也可以从wrapper里去getWorkResult
     * 
     * @param workerWrappers
     * @param e
     */
    void failure(List<WorkerWrapper> workerWrappers, Exception e);
}
