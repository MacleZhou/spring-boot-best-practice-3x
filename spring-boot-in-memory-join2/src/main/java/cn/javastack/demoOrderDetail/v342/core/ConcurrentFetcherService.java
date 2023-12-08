package cn.javastack.demoOrderDetail.v342.core;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ConcurrentFetcherService {
    private ExecutorService executorService;
    @Autowired
    private List<ItemFetcherExecutor> itemFetcherExecutors;

    @PostConstruct
    public void init(){
        this.executorService = Executors.newFixedThreadPool(20);
    }

    @SneakyThrows
    public <F extends ItemFetcher> void fetch(Class<F> cls, List<F> fetchers){
        if (CollectionUtils.isNotEmpty(fetchers)){
            // 创建异步执行任务
            List<Callable<Void>> callables = this.itemFetcherExecutors.stream()
                    .filter(itemFetcherExecutor -> itemFetcherExecutor.support(cls))
                    .map(itemFetcherExecutor -> (Callable<Void>) () -> {
                        itemFetcherExecutor.fetch(fetchers);
                        return null;
                    }).collect(Collectors.toList());
            // 线程池中并行执行
            this.executorService.invokeAll(callables);
        }
    }
}