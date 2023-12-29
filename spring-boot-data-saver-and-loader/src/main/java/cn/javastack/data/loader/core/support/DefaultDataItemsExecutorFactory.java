package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.annotation.DataHolderConfig;
import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.annotation.ExecutorType;
import cn.javastack.data.loader.core.DataItemExecutor;
import cn.javastack.data.loader.core.DataItemExecutorFactory;
import cn.javastack.data.loader.core.DataItemsExecutor;
import cn.javastack.data.loader.core.DataItemsExecutorFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

        if(CollectionUtils.isEmpty(dataItemExecutors)){
            throw new RuntimeException("NO EXECUTOR DEFINED ON CLASS " + cls.getCanonicalName());
        }

        // 封装为 JoinItemsExecutor
        return buildJoinItemsExecutor(cls, joinInMemoryConfig, dataHolderType, dataItemExecutors);
    }

    private <D> DataItemsExecutor<D> buildJoinItemsExecutor(Class<D> cls, DataHolderConfig joinInMemoryConfig, DataHolderType dataHolderType, List<DataItemExecutor<D>> dataItemExecutors){

        int maximalParallelTaskQuantity = calculateMaximalParallelTaskQuantity(joinInMemoryConfig, dataItemExecutors);

        // 使用 串行执行器
        //if(joinInMemoryConfig == null || joinInMemoryConfig.executorType() == LoaderExecutorType.SERIAL){
        if(maximalParallelTaskQuantity == 1){
            log.info("LoaderInMemory for {} use serial executor", cls);
            return new SerialDataItemsExecutor<>(cls, dataHolderType, dataItemExecutors);
        }

        // 使用 并行执行器
        //if (joinInMemoryConfig.executorType() == LoaderExecutorType.PARALLEL){
        if(maximalParallelTaskQuantity > 1) {
            ExecutorService executor = null;
            if(null != joinInMemoryConfig && StringUtils.isNoneBlank(joinInMemoryConfig.executorName())) {
                log.info("LoaderInMemory for {} use parallel executor, customized pool is {}", cls, joinInMemoryConfig.executorName());
                executor = executorServiceMap.get(joinInMemoryConfig.executorName());
            }
            else {
                if(!executorServiceMap.containsKey(cls.getCanonicalName())){
                    ExecutorService newexecutor = defaultExecutor(cls.getCanonicalName(), maximalParallelTaskQuantity);
                    executorServiceMap.put(cls.getCanonicalName(), newexecutor);
                }
                executor = executorServiceMap.get(cls.getCanonicalName());
                log.info("LoaderInMemory for {} use parallel executor, thread pool is framework defined", cls);
            }
            Preconditions.checkArgument(executor != null);
            return new ParallelDataItemsExecutor<>(cls, dataHolderType, dataItemExecutors, executor);
        }
        throw new IllegalArgumentException();
    }

    //确定是否并行
    private <D> int calculateMaximalParallelTaskQuantity(DataHolderConfig joinInMemoryConfig, List<DataItemExecutor<D>> dataItemExecutors){
        //默认并行，如果指定要串行，则串行
        if(null != joinInMemoryConfig && joinInMemoryConfig.executorType() == ExecutorType.SERIAL){
            return 1;
        }
        Map<Integer, Long> collect = dataItemExecutors.stream().collect(Collectors.groupingBy(DataItemExecutor::runOnLevel, Collectors.counting()));
        return collect.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    private <D> ExecutorService defaultExecutor(String name, int maxSize){
        BasicThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("DataLoaderInMemory-"+name+"-Thread-%d")
                .daemon(true)
                .build();
        //int maxSize = Runtime.getRuntime().availableProcessors() * 3;
        return new ThreadPoolExecutor(0, maxSize,
                600L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                basicThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
