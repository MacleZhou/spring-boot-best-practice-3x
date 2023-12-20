package cn.javastack.demo.v342.core;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetcherService {
    @Autowired
    private List<ItemFetcherExecutor> itemFetcherExecutors;

    public <F extends ItemFetcher> void fetch(Class<F> cls, List fetchers){
        if (CollectionUtils.isNotEmpty(fetchers)){
            this.itemFetcherExecutors.stream()
                    // 是否能处理该类型
                    .filter(itemFetcherExecutor -> itemFetcherExecutor.support(cls))
                    // 执行真正的绑定
                    .forEach(itemFetcherExecutor -> itemFetcherExecutor.fetch(fetchers));
        }
    }
}