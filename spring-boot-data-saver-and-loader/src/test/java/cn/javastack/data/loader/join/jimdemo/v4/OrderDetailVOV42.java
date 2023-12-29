package cn.javastack.data.loader.join.jimdemo.v4;

import cn.javastack.data.loader.annotation.DataHolderConfig;
import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.annotation.DataLoaderInMemory;
import cn.javastack.data.loader.join.jimdemo.vo.*;
import lombok.Data;


/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
@DataHolderConfig(dataHolderType = DataHolderType.JOIN, executorName = "orderDetailVOV42ExecutorService")
public class OrderDetailVOV42 extends OrderDetailVO {
    private final OrderVO order;

    @DataLoaderInMemory(keyFromSourceData = "#{order.userId}",
            keyFromJoinData = "#{id}",
            loader = "#{@userRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.join.jimdemo.vo.UserVO).apply(#root)}"
        )
    private UserVO user;

    @DataLoaderInMemory(keyFromSourceData = "#{order.addressId}",
            keyFromJoinData = "#{id}",
            loader = "#{@addressRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.join.jimdemo.vo.AddressVO).apply(#root)}"
    )
    private AddressVO address;

    @DataLoaderInMemory(keyFromSourceData = "#{order.productId}",
            keyFromJoinData = "#{id}",
            loader = "#{@productRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.join.jimdemo.vo.ProductVO).apply(#root)}"
    )
    private ProductVO product;
}
