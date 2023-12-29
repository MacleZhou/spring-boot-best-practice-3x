package cn.javastack.data.config;

import cn.javastack.data.loader.core.DataItemExecutorFactory;
import cn.javastack.data.loader.core.DataItemsExecutorFactory;
import cn.javastack.data.loader.core.DataLoaderInMemoryService;
import cn.javastack.data.loader.core.support.DefaultDataItemsExecutorFactory;
import cn.javastack.data.loader.core.support.DefaultDataLoaderInMemoryService;
import cn.javastack.data.loader.core.support.LoaderInMemoryBasedDataItemExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 配置Join & Aggregate到Spring容器中
 */
@Configuration
@Slf4j
public class DataLoaderInMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DataItemsExecutorFactory joinItemsExecutorFactory(Collection<? extends DataItemExecutorFactory> joinItemExecutorFactories,
                                                             Map<String, ExecutorService> executorServiceMap){
        return new DefaultDataItemsExecutorFactory(joinItemExecutorFactories, executorServiceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataLoaderInMemoryService joinService(DataItemsExecutorFactory dataItemsExecutorFactory){
        return new DefaultDataLoaderInMemoryService(dataItemsExecutorFactory);
    }

    @Bean
    public LoaderInMemoryBasedDataItemExecutorFactory joinInMemoryBasedJoinItemExecutorFactory(ApplicationContext applicationContext){
        return new LoaderInMemoryBasedDataItemExecutorFactory(new BeanFactoryResolver(applicationContext));
    }
}
