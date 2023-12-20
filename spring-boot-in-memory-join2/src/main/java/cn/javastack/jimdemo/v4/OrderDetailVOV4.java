package cn.javastack.jimdemo.v4;

import cn.javastack.jimdemo.vo.*;
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
            joinDataConverter = "#{T(cn.javastack.jimdemo.vo.UserVO).apply(#root)}"
        )
    private UserVO user;

    @JoinInMemory(keyFromSourceData = "#{order.addressId}",
            keyFromJoinData = "#{id}",
            loader = "#{@addressRepository.getByIds(#root)}",
            joinDataConverter = "#{T(cn.javastack.jimdemo.vo.AddressVO).apply(#root)}"
    )
    private AddressVO address;

    @JoinInMemory(keyFromSourceData = "#{order.productId}",
            keyFromJoinData = "#{id}",
            loader = "#{@productRepository.getByIds(#root)}",
            joinDataConverter = "#{T(cn.javastack.jimdemo.vo.ProductVO).apply(#root)}"
    )
    private ProductVO product;
}
