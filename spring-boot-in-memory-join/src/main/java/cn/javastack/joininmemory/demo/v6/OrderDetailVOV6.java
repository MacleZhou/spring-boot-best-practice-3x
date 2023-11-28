package cn.javastack.joininmemory.demo.v6;


import cn.javastack.annotation.joininmemory.JoinInMemeoryExecutorType;
import cn.javastack.annotation.joininmemory.JoinInMemoryConfig;
import cn.javastack.joininmemory.JoinAddressVOOnId;
import cn.javastack.joininmemory.JoinProductVOOnId;
import cn.javastack.joininmemory.JoinUserVOOnId;
import cn.javastack.joininmemory.demo.*;
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
