package cn.javastack.demo.v342.core;

import java.util.List;

/**
 * 此接口定义执行抽取接口
 * **/

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