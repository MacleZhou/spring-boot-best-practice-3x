package cn.javastack.data.loader.annotation;

/**
 * 串行还是并行Join
 */
public enum LoaderExecutorType {
    PARALLEL, // 并行执行
    SERIAL // 串行执行
}
