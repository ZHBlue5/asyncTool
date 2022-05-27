package com.zhblue.platform.worker;

import com.zhblue.platform.constant.ResultState;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 执行结果
 */
@Data
@NoArgsConstructor
public class WorkResult<V> {
    /**
     * 执行的结果
     */
    private V result;
    /**
     * 结果状态
     */
    private ResultState resultState;
    private Exception ex;

    public WorkResult(V result, ResultState resultState) {
        this(result, resultState, null);
    }

    public WorkResult(V result, ResultState resultState, Exception ex) {
        this.result = result;
        this.resultState = resultState;
        this.ex = ex;
    }

    public static <V> WorkResult<V> defaultResult() {
        return new WorkResult<>(null, ResultState.DEFAULT);
    }

}
