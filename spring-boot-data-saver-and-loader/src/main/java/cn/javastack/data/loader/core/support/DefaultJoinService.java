package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.core.JoinItemsExecutor;
import cn.javastack.data.loader.core.JoinItemsExecutorFactory;
import cn.javastack.data.loader.core.JoinService;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Join 服务对外接口
 */
public class DefaultJoinService implements JoinService {
    private final JoinItemsExecutorFactory joinItemsExecutorFactory;

    /**
     * 缓存，避免频繁的初始化
     */
    private final Map<Class, JoinItemsExecutor> cache = Maps.newConcurrentMap();

    public DefaultJoinService(JoinItemsExecutorFactory joinItemsExecutorFactory) {
        this.joinItemsExecutorFactory = joinItemsExecutorFactory;
    }

    @Override
    public <T> void joinInMemory(Class<T> tCls, List<T> t) {
        this.cache.computeIfAbsent(tCls, this::createJoinExecutorGroup)
                .execute(t);
    }

    @Override
    public <T> void register(Class<T> tCls) {
        this.cache.computeIfAbsent(tCls, this::createJoinExecutorGroup);
    }

    private JoinItemsExecutor createJoinExecutorGroup(Class aClass) {
        return this.joinItemsExecutorFactory.createFor(aClass);
    }
}
