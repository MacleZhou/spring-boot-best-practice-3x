package cn.javastack.joininmemory.core.support;


import cn.javastack.joininmemory.core.JoinItemsExecutor;
import cn.javastack.joininmemory.core.JoinItemsExecutorFactory;
import cn.javastack.joininmemory.core.JoinService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * Join 服务对外接口
 */
@Service("joinService")
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
