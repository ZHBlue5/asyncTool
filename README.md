#### 说明

参考京东的asyncTool，新增了部分功能

#### 基本组件

worker： 一个最小的任务执行单元。通常是一个网络调用，或一段耗时操作。

T，V两个泛型，分别是入参和出参类型。

譬如该耗时操作，入参是String，执行完毕的结果是Integer，那么就可以用泛型来定义。

多个不同的worker之间，没有关联，分别可以有不同的入参、出参类型。

```
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
```

callBack：对每个worker的回调。worker执行完毕后，会回调该接口，带着执行成功、失败、原始入参、和详细的结果。

```

/**
 * 每个执行单元执行完毕后，会回调该接口</p>
 * 需要监听执行结果的，实现该接口即可
 * @author ZHBlue
 * @since 2022/5/26 15:30
 */
@FunctionalInterface
public interface ICallback<T, V> {
    /**
     * 任务开始的监听
     */
    default void begin() {

    }

    /**
     * 耗时操作执行完毕后，就给value注入值
     * @param success 执行结果
     * @param param
     * @param workResult
     */
    void result(boolean success, T param, WorkResult<V> workResult);
}

```

wrapper：组合了worker和callback，是一个 **最小的调度单元** 。通过编排wrapper之间的关系，达到组合各个worker顺序的目的。

wrapper的泛型和worker的一样，决定了入参和结果的类型

```
        WorkerWrapper<String, String> workerWrapper = new WorkerWrapper<>(w, "0", w);
        WorkerWrapper<String, String> workerWrapper1 = new WorkerWrapper<>(w1, "1", w1);
        WorkerWrapper<String, String> workerWrapper2 = new WorkerWrapper<>(w2, "2", w2);
        WorkerWrapper<String, String> workerWrapper3 = new WorkerWrapper<>(w3, "3", w3);

```。
