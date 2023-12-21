package cn.javastack.aim.core;

import java.util.List;

/**
 * 创建数据的AggregateItemExecutor For class
 */
public interface AggregateItemExecutorFactory {
    <DATA> List<AggregateItemExecutor<DATA>> createForType(Class<DATA> cls);
}
