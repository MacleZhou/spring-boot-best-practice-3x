package cn.javastack.aim.config;

import cn.javastack.aim.core.AggregateItemExecutorFactory;
import cn.javastack.aim.core.AggregateItemsExecutorFactory;
import cn.javastack.aim.core.support.DefaultAggregateItemsExecutorFactory;
import cn.javastack.aim.core.support.AggregateInMemoryBasedAggregateItemExecutorFactory;
import cn.javastack.aim.core.AggregateService;
import cn.javastack.aim.core.support.DefaultAggregateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by taoli on 2022/8/6.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Configuration
@Slf4j
public class AggregateInMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AggregateItemsExecutorFactory aggregateItemsExecutorFactory(Collection<? extends AggregateItemExecutorFactory> joinItemExecutorFactories,
                                                                  Map<String, ExecutorService> executorServiceMap){
        return new DefaultAggregateItemsExecutorFactory(joinItemExecutorFactories, executorServiceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public AggregateService aggregateService(AggregateItemsExecutorFactory joinItemsExecutorFactory){
        return new DefaultAggregateService(joinItemsExecutorFactory);
    }

    @Bean
    public AggregateInMemoryBasedAggregateItemExecutorFactory aggregateInMemoryBasedAggregateItemExecutorFactory(ApplicationContext applicationContext){
        return new AggregateInMemoryBasedAggregateItemExecutorFactory(new BeanFactoryResolver(applicationContext));
    }

    @Bean
    public ExecutorService defaultExecutor(){
        BasicThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("AggregateInMemory-Thread-%d")
                .daemon(true)
                .build();
        int maxSize = Runtime.getRuntime().availableProcessors() * 3;
        return new ThreadPoolExecutor(0, maxSize,
                600L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                basicThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
