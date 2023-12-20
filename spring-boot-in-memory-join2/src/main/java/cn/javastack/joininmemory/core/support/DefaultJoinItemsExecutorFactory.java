package cn.javastack.joininmemory.core.support;


import cn.javastack.joininmemory.annotation.JoinInMemeoryExecutorType;
import cn.javastack.joininmemory.annotation.JoinInMemoryConfig;
import cn.javastack.joininmemory.core.JoinItemExecutor;
import cn.javastack.joininmemory.core.JoinItemExecutorFactory;
import cn.javastack.joininmemory.core.JoinItemsExecutor;
import cn.javastack.joininmemory.core.JoinItemsExecutorFactory;
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

/**
 * Created by taoli on 2022/8/5.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * 1. 使用 JoinItemExecutorFactory 为每个 Join 数据 创建 JoinItemExecutor <br />
 * 2. 将一个 class 的 JoinItemExecutor 封装成 JoinItemsExecutor
 */
@Slf4j
public class DefaultJoinItemsExecutorFactory implements JoinItemsExecutorFactory {
    private final List<JoinItemExecutorFactory> joinItemExecutorFactories;
    private final Map<String, ExecutorService> executorServiceMap;

    public DefaultJoinItemsExecutorFactory(Collection<? extends JoinItemExecutorFactory> joinItemExecutorFactories,
                                           Map<String, ExecutorService> executorServiceMap) {
        this.joinItemExecutorFactories = Lists.newArrayList(joinItemExecutorFactories);

        // 按执行顺序进行排序
        AnnotationAwareOrderComparator.sort(this.joinItemExecutorFactories);
        this.executorServiceMap = executorServiceMap;
        log.debug("jim.DefaultJoinItemsExecutorFactory.new=" + this.joinItemExecutorFactories.size());
        log.debug("jim.DefaultJoinItemsExecutorFactory.new=" + this.executorServiceMap.size());
    }

    private ExecutorService defaultExecutor(){
        BasicThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("JoinInMemory-Thread-%d")
                .daemon(true)
                .build();
        int maxSize = Runtime.getRuntime().availableProcessors() * 3;
        log.debug("jim.DefaultJoinItemsExecutorFactory.defaultExecutor.new");
        return new ThreadPoolExecutor(0, maxSize,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                basicThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public <D> JoinItemsExecutor<D> createFor(Class<D> cls) {
        log.debug("jim.DefaultJoinItemsExecutorFactory.createFor.begin");
        // 依次遍历 JoinItemExecutorFactory， 收集 JoinItemExecutor 信息
        List<JoinItemExecutor<D>> joinItemExecutors = this.joinItemExecutorFactories.stream()
                .flatMap(factory -> factory.createForType(cls).stream())
                .collect(Collectors.toList());

        log.debug("jim.DefaultJoinItemsExecutorFactory.createFor.size=" + joinItemExecutors.size());

        // 从 class 上读取配置信息
        JoinInMemoryConfig joinInMemoryConfig = cls.getAnnotation(JoinInMemoryConfig.class);
        log.debug("jim.DefaultJoinItemsExecutorFactory.createFor.joinInMemoryConfig=" + (null == joinInMemoryConfig ? "null" : "not-null"));

        // 封装为 JoinItemsExecutor
        return buildJoinItemsExecutor(cls, joinInMemoryConfig, joinItemExecutors);
    }

    private  <D> JoinItemsExecutor<D> buildJoinItemsExecutor(Class<D> cls, JoinInMemoryConfig joinInMemoryConfig, List<JoinItemExecutor<D>> joinItemExecutors){
        log.debug("jim.DefaultJoinItemsExecutorFactory.buildJoinItemsExecutor.begin: " + joinItemExecutors.size());
        // 使用 串行执行器
        if(joinInMemoryConfig == null || joinInMemoryConfig.executorType() == JoinInMemeoryExecutorType.SERIAL){
            log.info("jim.DefaultJoinItemsExecutorFactory.buildJoinItemsExecutor.JoinInMemory for {} use serial executor", cls);
            return new SerialJoinItemsExecutor<>(cls, joinItemExecutors);
        }

        // 使用 并行执行器
        if (joinInMemoryConfig.executorType() == JoinInMemeoryExecutorType.PARALLEL){
            log.info("jim.DefaultJoinItemsExecutorFactory.buildJoinItemsExecutor.JoinInMemory for {} use parallel executor, pool is {}", cls, joinInMemoryConfig.executorName());
            ExecutorService executor = executorServiceMap.get(joinInMemoryConfig.executorName());
            //Preconditions.checkArgument(executor != null);
            if(null == executor) {//如果没有配置线程池，则采用默认线程池
                executor = defaultExecutor();
            }
            return new ParallelJoinItemsExecutor<>(cls, joinItemExecutors, executor);
        }
        throw new IllegalArgumentException();
    }
}
