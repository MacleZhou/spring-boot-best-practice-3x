package cn.javastack.aggregateinmemory.core.support;


import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemExecutorFactory;
import cn.javastack.aggregateinmemory.annotation.AggregateInMemoryExecutorType;
import cn.javastack.aggregateinmemory.annotation.AggregateInMemoryConfig;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutorFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by taoli on 2022/8/5.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * 1. 使用 JoinItemExecutorFactory 为每个 Join 数据 创建 JoinItemExecutor <br />
 * 2. 将一个 class 的 JoinItemExecutor 封装成 JoinItemsExecutor
 */
@Slf4j
public class DefaultAggregateItemsExecutorFactory implements AggregateItemsExecutorFactory {
    private final List<AggregateItemExecutorFactory> joinItemExecutorFactories;
    private final Map<String, ExecutorService> executorServiceMap;

    public DefaultAggregateItemsExecutorFactory(Collection<? extends AggregateItemExecutorFactory> joinItemExecutorFactories,
                                                Map<String, ExecutorService> executorServiceMap) {
        this.joinItemExecutorFactories = Lists.newArrayList(joinItemExecutorFactories);
        // 按执行顺序进行排序
        AnnotationAwareOrderComparator.sort(this.joinItemExecutorFactories);
        this.executorServiceMap = executorServiceMap;
    }


    @Override
    public <D> AggregateItemsExecutor<D> createFor(Class<D> cls) {
        // 依次遍历 JoinItemExecutorFactory， 收集 JoinItemExecutor 信息
        List<AggregateItemExecutor<D>> joinItemExecutors = this.joinItemExecutorFactories.stream()
                .flatMap(factory -> factory.createForType(cls).stream())
                .collect(Collectors.toList());

        // 从 class 上读取配置信息
        AggregateInMemoryConfig joinInMemoryConfig = cls.getAnnotation(AggregateInMemoryConfig.class);

        // 封装为 JoinItemsExecutor
        return buildJoinItemsExecutor(cls, joinInMemoryConfig, joinItemExecutors);
    }

    private  <D> AggregateItemsExecutor<D> buildJoinItemsExecutor(Class<D> cls, AggregateInMemoryConfig joinInMemoryConfig, List<AggregateItemExecutor<D>> joinItemExecutors){
        // 使用 串行执行器
        if(joinInMemoryConfig == null || joinInMemoryConfig.executorType() == AggregateInMemoryExecutorType.SERIAL){
            log.info("JoinInMemory for {} use serial executor", cls);
            return new SerialAggregateItemsExecutor<>(cls, joinItemExecutors);
        }

        // 使用 并行执行器
        if (joinInMemoryConfig.executorType() == AggregateInMemoryExecutorType.PARALLEL){
            log.info("JoinInMemory for {} use parallel executor, pool is {}", cls, joinInMemoryConfig.executorName());
            ExecutorService executor = executorServiceMap.get(joinInMemoryConfig.executorName());
            Preconditions.checkArgument(executor != null);
            return new ParallelAggregateItemsExecutor<>(cls, joinItemExecutors, executor);
        }
        throw new IllegalArgumentException();
    }
}
