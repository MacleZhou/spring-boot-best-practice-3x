package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.core.DataItemsExecutor;
import cn.javastack.data.loader.core.DataItemsExecutorFactory;
import cn.javastack.data.loader.core.DataLoaderInMemoryService;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Join 服务对外接口
 */
public class DefaultDataLoaderInMemoryService implements DataLoaderInMemoryService {
    private final DataItemsExecutorFactory dataItemsExecutorFactory;

    /**
     * 缓存，避免频繁的初始化
     */
    private final Map<Class, DataItemsExecutor> cache = Maps.newConcurrentMap();

    public DefaultDataLoaderInMemoryService(DataItemsExecutorFactory dataItemsExecutorFactory) {
        this.dataItemsExecutorFactory = dataItemsExecutorFactory;
    }

    @Override
    public <T> void loaderInMemory(Class<T> tCls, List<T> t) {
        this.cache.computeIfAbsent(tCls, this::createJoinExecutorGroup)
                .execute(t);
    }

    @Override
    public <T> void register(Class<T> tCls) {
        this.cache.computeIfAbsent(tCls, this::createJoinExecutorGroup);
    }

    private DataItemsExecutor createJoinExecutorGroup(Class aClass) {
        return this.dataItemsExecutorFactory.createFor(aClass);
    }
}
