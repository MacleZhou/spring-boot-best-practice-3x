package cn.javastack.demo.v6;


import cn.javastack.demo.annotation.JoinAddressVOOnId;
import cn.javastack.demo.annotation.JoinProductVOOnId;
import cn.javastack.demo.annotation.JoinUserVOOnId;
import cn.javastack.demo.vo.*;
import cn.javastack.joininmemory.annotation.JoinInMemeoryExecutorType;
import cn.javastack.joininmemory.annotation.JoinInMemoryConfig;
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
