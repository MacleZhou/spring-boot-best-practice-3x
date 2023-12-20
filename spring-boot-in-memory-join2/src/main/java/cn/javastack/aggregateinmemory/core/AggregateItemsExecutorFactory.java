package cn.javastack.aggregateinmemory.core;

/**
 * 工厂类，从 class 中解析信息，并创建 AggregateItemExecutor
 */
public interface AggregateItemsExecutorFactory {
    /**
     * 为 类 创建 Aggregate 执行器
     * @param cls
     * @param <D>
     * @return
     */
    <D> AggregateItemsExecutor<D> createFor(Class<D> cls);
}
