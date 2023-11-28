package cn.javastack.joininmemory.demo;

import cn.javastack.service.order.Order;
import lombok.Value;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Value
public class OrderVO {
    private Long id;
    private Long userId;
    private Long addressId;
    private Long productId;

    public static OrderVO apply(Order order){
        if (order == null){
            return null;
        }

        return new OrderVO(order.getId(), order.getUserId(), order.getAddressId(), order.getProductId());
    }
}
