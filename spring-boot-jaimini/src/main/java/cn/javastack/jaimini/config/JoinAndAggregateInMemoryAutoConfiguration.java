package cn.javastack.jaimini.config;

import cn.javastack.jaimini.aim.core.AggregateItemExecutorFactory;
import cn.javastack.jaimini.aim.core.AggregateItemsExecutorFactory;
import cn.javastack.jaimini.aim.core.AggregateService;
import cn.javastack.jaimini.aim.core.support.AggregateInMemoryBasedAggregateItemExecutorFactory;
import cn.javastack.jaimini.aim.core.support.DefaultAggregateItemsExecutorFactory;
import cn.javastack.jaimini.aim.core.support.DefaultAggregateService;
import cn.javastack.data.loader.core.JoinItemExecutorFactory;
import cn.javastack.data.loader.core.JoinItemsExecutorFactory;
import cn.javastack.data.loader.core.JoinService;
import cn.javastack.data.loader.core.support.DefaultJoinItemsExecutorFactory;
import cn.javastack.data.loader.core.support.DefaultJoinService;
import cn.javastack.data.loader.core.support.JoinInMemoryBasedJoinItemExecutorFactory;
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
 * 配置Join & Aggregate到Spring容器中
 */
@Configuration
@Slf4j
public class JoinAndAggregateInMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JoinItemsExecutorFactory joinItemsExecutorFactory(Collection<? extends JoinItemExecutorFactory> joinItemExecutorFactories,
                                                             Map<String, ExecutorService> executorServiceMap){
        return new DefaultJoinItemsExecutorFactory(joinItemExecutorFactories, executorServiceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public JoinService joinService(JoinItemsExecutorFactory joinItemsExecutorFactory){
        return new DefaultJoinService(joinItemsExecutorFactory);
    }

    @Bean
    public JoinInMemoryBasedJoinItemExecutorFactory joinInMemoryBasedJoinItemExecutorFactory(ApplicationContext applicationContext){
        return new JoinInMemoryBasedJoinItemExecutorFactory(new BeanFactoryResolver(applicationContext));
    }

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
                .namingPattern("JoinAndAggregateInMemory-Thread-%d")
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
