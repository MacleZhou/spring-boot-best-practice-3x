package cn.javastack.data.saver.annotation;

public @interface ChildData {

    /**
     * 定义的Child table对应的data字段
     * */
    String value();

    /**
     * 如何找到所有的Children数据
     */
    String fetchChildren();
}
