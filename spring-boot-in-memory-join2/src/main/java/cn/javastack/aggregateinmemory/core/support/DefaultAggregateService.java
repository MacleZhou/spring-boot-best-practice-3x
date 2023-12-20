package cn.javastack.aggregateinmemory.core.support;

import cn.javastack.aggregateinmemory.core.AggregateItemsExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutorFactory;
import cn.javastack.aggregateinmemory.core.AggregateService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultAggregateService implements AggregateService {
    private final AggregateItemsExecutorFactory aggregateItemsExecutorFactory;

    /**
     * 缓存，避免频繁的初始化
     */
    private final Map<Class, AggregateItemsExecutor> cache = Maps.newConcurrentMap();

    public DefaultAggregateService(AggregateItemsExecutorFactory aggregateItemsExecutorFactory) {
        this.aggregateItemsExecutorFactory = aggregateItemsExecutorFactory;
    }

    @Override
    public <T> void aggregateInMemory(Class<T> tCls, List<T> t) {
        this.cache.computeIfAbsent(tCls, this::createAggregateExecutorGroup)
                .execute(t);
    }

    @Override
    public <T> void register(Class<T> tCls) {
        this.cache.computeIfAbsent(tCls, this::createAggregateExecutorGroup);
    }

    private AggregateItemsExecutor createAggregateExecutorGroup(Class aClass) {
        return this.aggregateItemsExecutorFactory.createFor(aClass);
    }
}
