package cn.javastack.data.loader.core;

import java.util.List;

/**
 * 接口执行一个field的内存join
 */
public interface DataItemsExecutor<DATA> {
    void execute(List<DATA> datas);
}
