package com.zhblue.platform.callback;

import com.zhblue.platform.worker.WorkResult;

/**
 * 默认回调类
 * @author ZHBlue
 * @since 2022/5/26 15:32
 */
public class DefaultCallback<T, V>  implements ICallback<T, V> {
    /**
     * 任务开始的监听
     */
    @Override
    public void begin() {
    }

    /**
     * 耗时操作执行完毕后，就给value注入值
     *
     * @param success    执行结果
     * @param param
     * @param workResult
     */
    @Override
    public void result(boolean success, T param, WorkResult<V> workResult) {

    }
}
