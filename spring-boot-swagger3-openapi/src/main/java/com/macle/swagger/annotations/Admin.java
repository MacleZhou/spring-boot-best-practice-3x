package com.macle.swagger.annotations;

import java.lang.annotation.*;

// 代表一个API只能被Admin调用
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Admin {
}
