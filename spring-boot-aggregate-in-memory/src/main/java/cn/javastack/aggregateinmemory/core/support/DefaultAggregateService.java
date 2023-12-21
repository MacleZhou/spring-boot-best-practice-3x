package cn.javastack.aggregateinmemory.core.support;


import cn.javastack.aggregateinmemory.core.AggregateItemsExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutorFactory;
import cn.javastack.aggregateinmemory.core.AggregateService;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * Join 服务对外接口
 */
public class DefaultAggregateService implements AggregateService {
    private final AggregateItemsExecutorFactory joinItemsExecutorFactory;

    /**
     * 缓存，避免频繁的初始化
     */
    private final Map<Class, AggregateItemsExecutor> cache = Maps.newConcurrentMap();

    public DefaultAggregateService(AggregateItemsExecutorFactory joinItemsExecutorFactory) {
        this.joinItemsExecutorFactory = joinItemsExecutorFactory;
    }

    @Override
    public <T> void aggregateInMemory(Class<T> tCls, List<T> t) {
        this.cache.computeIfAbsent(tCls, this::createJoinExecutorGroup)
                .execute(t);
    }

    @Override
    public <T> void register(Class<T> tCls) {
        this.cache.computeIfAbsent(tCls, this::createJoinExecutorGroup);
    }

    private AggregateItemsExecutor createJoinExecutorGroup(Class aClass) {
        return this.joinItemsExecutorFactory.createFor(aClass);
    }
}
