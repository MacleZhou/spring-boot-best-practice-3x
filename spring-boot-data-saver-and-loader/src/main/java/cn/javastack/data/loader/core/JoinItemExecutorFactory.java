package cn.javastack.data.loader.core;

import cn.javastack.data.loader.annotation.DataHolderType;

import java.util.List;

/**
 * 工厂接口，根据类创建类的所有执行器
 */
public interface JoinItemExecutorFactory {
    <DATA> List<JoinItemExecutor<DATA>> createForType(Class<DATA> cls, DataHolderType dataHolderType);
}
