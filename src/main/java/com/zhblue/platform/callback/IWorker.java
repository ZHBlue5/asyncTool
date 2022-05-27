package com.zhblue.platform.callback;

import java.util.Map;

import com.zhblue.platform.wrapper.WorkerWrapper;

/**
 * 每个最小执行单元需要实现该接口
 * 
 * @author ZHBlue
 * @since 2022/5/26 15:40
 */
@FunctionalInterface
public interface IWorker<T, V> {
    /**
     * 在这里做耗时操作，如rpc请求、IO等
     *
     * @param object
     *            object
     * @param allWrappers
     *            任务包装
     */
    V action(T object, Map<String, WorkerWrapper> allWrappers);

    /**
     * 超时、异常时，返回的默认值
     *
     * @return 默认值
     */
    default V defaultValue() {
        return null;
    }
}
