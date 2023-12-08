package cn.javastack.demoOrderDetail.v342.core;

/**
 * 此接口定义一个抽取的记录的主键
 * */
public interface ItemFetcher<RESULT> {

    /**
     * 保存抽取结果回到聚合的VO中
     * */
    void setResult(RESULT result);
}
