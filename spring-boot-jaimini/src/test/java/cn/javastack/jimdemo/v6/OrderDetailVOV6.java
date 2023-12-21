package cn.javastack.jimdemo.v6;


import cn.javastack.jaimini.jim.annotation.JoinInMemeoryExecutorType;
import cn.javastack.jaimini.jim.annotation.JoinInMemoryConfig;
import cn.javastack.jimdemo.annotation.JoinAddressVOOnId;
import cn.javastack.jimdemo.annotation.JoinProductVOOnId;
import cn.javastack.jimdemo.annotation.JoinUserVOOnId;
import cn.javastack.jimdemo.vo.*;
import lombok.Data;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
@JoinInMemoryConfig(executorType = JoinInMemeoryExecutorType.PARALLEL)
public class OrderDetailVOV6 extends OrderDetailVO {
    private final OrderVO order;

    @JoinUserVOOnId(keyFromSourceData = "#{order.userId}")
    private UserVO user;

    @JoinAddressVOOnId(keyFromSourceData = "#{order.addressId}")
    private AddressVO address;

    @JoinProductVOOnId(keyFromSourceData = "#{order.productId}")
    private ProductVO product;

}
