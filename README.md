#### 说明

参考京东的asyncTool，新增了部分功能

#### 基本组件

worker： 一个最小的任务执行单元。通常是一个网络调用，或一段耗时操作。

T，V两个泛型，分别是入参和出参类型。

譬如该耗时操作，入参是String，执行完毕的结果是Integer，那么就可以用泛型来定义。

多个不同的worker之间，没有关联，分别可以有不同的入参、出参类型。

callBack：对每个worker的回调。worker执行完毕后，会回调该接口，带着执行成功、失败、原始入参、和详细的结果。

wrapper：组合了worker和callback，是一个 **最小的调度单元** 。通过编排wrapper之间的关系，达到组合各个worker顺序的目的。

wrapper的泛型和worker的一样，决定了入参和结果的类型。