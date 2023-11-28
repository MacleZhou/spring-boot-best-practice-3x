package cn.javastack.joininmemory.demo.v5;


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
public class OrderDetailVOV5 extends OrderDetailVO {
    private final OrderVO order;

    @JoinUserVOOnId(keyFromSourceData = "#{order.userId}")
    private UserVO user;

    @JoinAddressVOOnId(keyFromSourceData = "#{order.addressId}")
    private AddressVO address;

    @JoinProductVOOnId(keyFromSourceData = "#{order.productId}")
    private ProductVO product;

}
