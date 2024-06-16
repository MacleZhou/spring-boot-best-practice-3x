package com.macle.swagger2.annotations;

import java.lang.annotation.*;

// 代表一个被支持的流程类型
@Repeatable(FlowTypes.class)
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowType {
    // 流程类型的名称
    String value();
    // 发起该流程时候, 需要传递的参数类型. 很明显, 我们建议使用自定义类型封装所有的请求参数
    Class<?> paramType();
}
