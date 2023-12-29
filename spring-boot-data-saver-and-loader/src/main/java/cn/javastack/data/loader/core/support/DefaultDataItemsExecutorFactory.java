package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.annotation.LoaderExecutorType;
import cn.javastack.data.loader.annotation.DataHolderConfig;
import cn.javastack.data.loader.core.DataItemExecutor;
import cn.javastack.data.loader.core.DataItemExecutorFactory;
import cn.javastack.data.loader.core.DataItemsExecutor;
import cn.javastack.data.loader.core.DataItemsExecutorFactory;
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
 * 1. 使用 JoinItemExecutorFactory 为每个 Join 数据 创建 JoinItemExecutor <br />
 * 2. 将一个 class 的 JoinItemExecutor 封装成 JoinItemsExecutor
 */
@Slf4j
public class DefaultDataItemsExecutorFactory implements DataItemsExecutorFactory {
    private final List<DataItemExecutorFactory> joinItemExecutorFactories;
    private final Map<String, ExecutorService> executorServiceMap;

    public DefaultDataItemsExecutorFactory(Collection<? extends DataItemExecutorFactory> joinItemExecutorFactories,
                                           Map<String, ExecutorService> executorServiceMap) {
        this.joinItemExecutorFactories = Lists.newArrayList(joinItemExecutorFactories);
        // 按执行顺序进行排序
        AnnotationAwareOrderComparator.sort(this.joinItemExecutorFactories);
        this.executorServiceMap = executorServiceMap;
    }


    @Override
    public <D> DataItemsExecutor<D> createFor(Class<D> cls) {
        // 从 class 上读取配置信息
        DataHolderConfig joinInMemoryConfig = cls.getAnnotation(DataHolderConfig.class);
        DataHolderType dataHolderType = (null != joinInMemoryConfig) ? joinInMemoryConfig.dataHolderType() : DataHolderType.AGGREGATE;

        // 依次遍历 JoinItemExecutorFactory， 收集 JoinItemExecutor 信息
        List<DataItemExecutor<D>> dataItemExecutors = this.joinItemExecutorFactories.stream()
                .flatMap(factory -> factory.createForType(cls, dataHolderType).stream())
                .collect(Collectors.toList());

        // 封装为 JoinItemsExecutor
        return buildJoinItemsExecutor(cls, joinInMemoryConfig, dataHolderType, dataItemExecutors);
    }

    private  <D> DataItemsExecutor<D> buildJoinItemsExecutor(Class<D> cls, DataHolderConfig joinInMemoryConfig, DataHolderType dataHolderType, List<DataItemExecutor<D>> dataItemExecutors){
        // 使用 串行执行器
        if(joinInMemoryConfig == null || joinInMemoryConfig.executorType() == LoaderExecutorType.SERIAL){
            log.info("JoinInMemory for {} use serial executor", cls);
            return new SerialDataItemsExecutor<>(cls, dataHolderType, dataItemExecutors);
        }

        // 使用 并行执行器
        if (joinInMemoryConfig.executorType() == LoaderExecutorType.PARALLEL){
            log.info("JoinInMemory for {} use parallel executor, pool is {}", cls, joinInMemoryConfig.executorName());
            ExecutorService executor = executorServiceMap.get(joinInMemoryConfig.executorName());
            Preconditions.checkArgument(executor != null);
            return new ParallelDataItemsExecutor<>(cls, dataHolderType, dataItemExecutors, executor);
        }
        throw new IllegalArgumentException();
    }
}
