package cn.javastack.aggregateinmemory.core.support;


import cn.javastack.aggregateinmemory.annotation.AggregateInMemeoryExecutorType;
import cn.javastack.aggregateinmemory.annotation.AggregateInMemoryConfig;
import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemExecutorFactory;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutorFactory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private ExecutorService defaultExecutor(){
        BasicThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("JoinInMemory-Thread-%d")
                .daemon(true)
                .build();
        int maxSize = Runtime.getRuntime().availableProcessors() * 3;
        return new ThreadPoolExecutor(0, maxSize,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                basicThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public <D> AggregateItemsExecutor<D> createFor(Class<D> cls) {
        // 依次遍历 JoinItemExecutorFactory， 收集 JoinItemExecutor 信息
        List<AggregateItemExecutor<D>> aggregateItemExecutors = this.aggregateItemExecutorFactories.stream()
                .flatMap(factory -> factory.createForType(cls).stream())
                .collect(Collectors.toList());

        // 从 class 上读取配置信息
        AggregateInMemoryConfig aggregateInMemoryConfig = cls.getAnnotation(AggregateInMemoryConfig.class);

        // 封装为 JoinItemsExecutor
        return buildJoinItemsExecutor(cls, aggregateInMemoryConfig, aggregateItemExecutors);
    }

    private  <D> AggregateItemsExecutor<D> buildJoinItemsExecutor(Class<D> cls, AggregateInMemoryConfig aggregateInMemoryConfig, List<AggregateItemExecutor<D>> aggregateItemExecutors){
        // 使用 串行执行器
        if(aggregateInMemoryConfig == null || aggregateInMemoryConfig.executorType() == AggregateInMemeoryExecutorType.SERIAL){
            log.info("JoinInMemory for {} use serial executor", cls);
            return new SerialAggregateItemsExecutor<>(cls, aggregateItemExecutors);
        }

        // 使用 并行执行器
        if (aggregateInMemoryConfig.executorType() == AggregateInMemeoryExecutorType.PARALLEL){
            log.info("JoinInMemory for {} use parallel executor, pool is {}", cls, aggregateInMemoryConfig.executorName());
            ExecutorService executor = executorServiceMap.get(aggregateInMemoryConfig.executorName());
            //Preconditions.checkArgument(executor != null);
            if(null == executor) {//如果没有配置线程池，则采用默认线程池
                executor = defaultExecutor();
            }
            return new ParallelAggregateItemsExecutor<>(cls, aggregateItemExecutors, executor);
        }
        throw new IllegalArgumentException();
    }
}
