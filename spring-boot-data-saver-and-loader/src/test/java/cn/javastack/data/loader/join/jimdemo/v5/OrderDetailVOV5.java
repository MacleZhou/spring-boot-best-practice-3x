package cn.javastack.data.loader.join.jimdemo.v5;


import cn.javastack.data.loader.annotation.DataHolderConfig;
import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.join.jimdemo.annotation.JoinAddressVOOnId;
import cn.javastack.data.loader.join.jimdemo.annotation.JoinProductVOOnId;
import cn.javastack.data.loader.join.jimdemo.annotation.JoinUserVOOnId;
import cn.javastack.data.loader.join.jimdemo.vo.*;
import lombok.Data;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
@DataHolderConfig(dataHolderType = DataHolderType.JOIN)
public class OrderDetailVOV5 extends OrderDetailVO {
    private final OrderVO order;

    @JoinUserVOOnId(keyFromSourceData = "#{order.userId}")
    private UserVO user;

    @JoinAddressVOOnId(keyFromSourceData = "#{order.addressId}")
    private AddressVO address;

    @JoinProductVOOnId(keyFromSourceData = "#{order.productId}")
    private ProductVO product;

}
