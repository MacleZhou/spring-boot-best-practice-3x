package cn.javastack.jaimini.jim.core;

import java.util.List;

/**
 * 接口执行一个field的内存join
 */
public interface JoinItemsExecutor<DATA> {
    void execute(List<DATA> datas);
}
