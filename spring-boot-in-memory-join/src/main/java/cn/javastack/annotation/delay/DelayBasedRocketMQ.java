package cn.javastack.annotation.delay;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by taoli on 2022/9/4.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelayBasedRocketMQ {
    /**
     * RocketMQ topic
     * @return
     */
    String topic();

    /**
     * Tag
     * @return
     */
    String tag() default "*";

    /**
     * 延迟级别
     * @return
     */
    int delayLevel() default -1;

    /**
     * 延迟时间 SpEL 表达式
     * @return
     */
    String delayLevelSpEl() default "0";

    /**
     * nameServer 配置
     * @return
     */
    String nameServer() default "${rocketmq.name-server:}";

    /**
     * 消费者组信息
     * @return
     */
    String consumerGroup();

    /**
     * 消费者运行的 profile，主要用于发送和消费分离的场景
     * @return
     */
    String consumerProfile() default "";
}
