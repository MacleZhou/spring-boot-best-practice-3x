package cn.javastack.demo.v4;


import cn.javastack.demo.vo.*;
import cn.javastack.joininmemory.annotation.JoinInMemory;
import lombok.Data;


/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
public class OrderDetailVOV4 extends OrderDetailVO {
    private final OrderVO order;

    @JoinInMemory(keyFromSourceData = "#{order.userId}",
            keyFromJoinData = "#{id}",
            loader = "#{@userRepository.getByIds(#root)}",
            joinDataConverter = "#{T(cn.javastack.joininmemory.demo.UserVO).apply(#root)}"
        )
    private UserVO user;

    @JoinInMemory(keyFromSourceData = "#{order.addressId}",
            keyFromJoinData = "#{id}",
            loader = "#{@addressRepository.getByIds(#root)}",
            joinDataConverter = "#{T(cn.javastack.joininmemory.demo.AddressVO).apply(#root)}"
    )
    private AddressVO address;

    @JoinInMemory(keyFromSourceData = "#{order.productId}",
            keyFromJoinData = "#{id}",
            loader = "#{@productRepository.getByIds(#root)}",
            joinDataConverter = "#{T(cn.javastack.joininmemory.demo.ProductVO).apply(#root)}"
    )
    private ProductVO product;
}
