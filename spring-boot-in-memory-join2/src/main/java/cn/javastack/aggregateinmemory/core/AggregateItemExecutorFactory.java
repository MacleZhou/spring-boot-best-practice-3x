package cn.javastack.aggregateinmemory.core;

import java.util.List;

public interface AggregateItemExecutorFactory {
    <DATA> List<AggregateItemExecutor<DATA>> createForType(Class<DATA> cls);
}
