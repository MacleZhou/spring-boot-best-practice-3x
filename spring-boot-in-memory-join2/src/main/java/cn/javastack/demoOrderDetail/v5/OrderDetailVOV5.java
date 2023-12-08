package cn.javastack.demoOrderDetail.v5;


import cn.javastack.demoOrderDetail.annotation.JoinAddressVOOnId;
import cn.javastack.demoOrderDetail.annotation.JoinProductVOOnId;
import cn.javastack.demoOrderDetail.annotation.JoinUserVOOnId;

import cn.javastack.demoOrderDetail.vo.*;
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
