package cn.javastack.domain.v6.fetcher;

import java.util.List;

public interface ItemFetcherExecutor<F extends ItemFetcher> {
    /**
     * 该组件是否能处理 cls 类型
     * @param cls
     * @return
     */
    boolean support(Class<F> cls);

    /**
     *  执行真正的数据绑定
     * @param fetchers
     */
    void fetch(List<F> fetchers);
}