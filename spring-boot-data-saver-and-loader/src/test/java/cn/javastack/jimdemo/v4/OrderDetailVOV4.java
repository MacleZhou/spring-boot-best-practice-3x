package cn.javastack.jimdemo.v4;

import cn.javastack.data.loader.annotation.DataHolderConfig;
import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.annotation.LoadDataToField;
import cn.javastack.jimdemo.vo.*;
import lombok.Data;


/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
@DataHolderConfig(dataHolderType = DataHolderType.JOIN)
public class OrderDetailVOV4 extends OrderDetailVO {
    private final OrderVO order;

    @LoadDataToField(keyFromSourceData = "#{order.userId}",
            keyFromJoinData = "#{id}",
            loader = "#{@userRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.jimdemo.vo.UserVO).apply(#root)}"
        )
    private UserVO user;

    @LoadDataToField(keyFromSourceData = "#{order.addressId}",
            keyFromJoinData = "#{id}",
            loader = "#{@addressRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.jimdemo.vo.AddressVO).apply(#root)}"
    )
    private AddressVO address;

    @LoadDataToField(keyFromSourceData = "#{order.productId}",
            keyFromJoinData = "#{id}",
            loader = "#{@productRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.jimdemo.vo.ProductVO).apply(#root)}"
    )
    private ProductVO product;
}
