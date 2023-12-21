package cn.javastack.jaimini.aim.core.support;


import cn.javastack.jaimini.aim.annotation.AggregateInMemoryConfig;
import cn.javastack.jaimini.aim.annotation.AggregateInMemoryExecutorType;
import cn.javastack.jaimini.aim.core.AggregateItemExecutor;
import cn.javastack.jaimini.aim.core.AggregateItemExecutorFactory;
import cn.javastack.jaimini.aim.core.AggregateItemsExecutor;
import cn.javastack.jaimini.aim.core.AggregateItemsExecutorFactory;
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
 *
 * 1. 使用 AggregateItemExecutorFactory 为每个 Aggregate 数据 创建 AggregateItemExecutor <br />
 * 2. 将一个 class 的 AggregateItemExecutor 封装成 AggregateItemsExecutor
 */
@Slf4j
public class DefaultAggregateItemsExecutorFactory implements AggregateItemsExecutorFactory {
    private final List<AggregateItemExecutorFactory> aggregateItemExecutorFactories;
    private final Map<String, ExecutorService> executorServiceMap;

    public DefaultAggregateItemsExecutorFactory(Collection<? extends AggregateItemExecutorFactory> aggregateItemExecutorFactories,
                                                Map<String, ExecutorService> executorServiceMap) {
        this.aggregateItemExecutorFactories = Lists.newArrayList(aggregateItemExecutorFactories);
        // 按执行顺序进行排序
        AnnotationAwareOrderComparator.sort(this.aggregateItemExecutorFactories);
        this.executorServiceMap = executorServiceMap;
    }


    @Override
    public <D> AggregateItemsExecutor<D> createFor(Class<D> cls) {
        // 依次遍历 JoinItemExecutorFactory， 收集 JoinItemExecutor 信息
        List<AggregateItemExecutor<D>> joinItemExecutors = this.aggregateItemExecutorFactories.stream()
                .flatMap(factory -> factory.createForType(cls).stream())
                .collect(Collectors.toList());

        // 从 class 上读取配置信息
        AggregateInMemoryConfig joinInMemoryConfig = cls.getAnnotation(AggregateInMemoryConfig.class);

        // 封装为 JoinItemsExecutor
        return buildAggregateItemsExecutor(cls, joinInMemoryConfig, joinItemExecutors);
    }

    private  <D> AggregateItemsExecutor<D> buildAggregateItemsExecutor(Class<D> cls, AggregateInMemoryConfig joinInMemoryConfig, List<AggregateItemExecutor<D>> joinItemExecutors){
        // 使用 串行执行器
        if(joinInMemoryConfig == null || joinInMemoryConfig.executorType() == AggregateInMemoryExecutorType.SERIAL){
            log.info("AggregateInMemory for {} use serial executor", cls);
            return new SerialAggregateItemsExecutor<>(cls, joinItemExecutors);
        }

        // 使用 并行执行器
        if (joinInMemoryConfig.executorType() == AggregateInMemoryExecutorType.PARALLEL){
            log.info("AggregateInMemory for {} use parallel executor, pool is {}", cls, joinInMemoryConfig.executorName());
            ExecutorService executor = executorServiceMap.get(joinInMemoryConfig.executorName());
            Preconditions.checkArgument(executor != null);
            return new ParallelAggregateItemsExecutor<>(cls, joinItemExecutors, executor);
        }
        throw new IllegalArgumentException();
    }
}
