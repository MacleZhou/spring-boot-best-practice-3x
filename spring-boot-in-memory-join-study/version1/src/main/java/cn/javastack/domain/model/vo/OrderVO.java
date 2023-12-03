package cn.javastack.domain.model.vo;

import cn.javastack.domain.model.entity.Order;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderVO {
    private Long id;
    private Long userId;
    private Long addressId;
    private Long productId;
    private String descr;

    private OrderVO(){}

    public static OrderVO apply(Order order){
        return new OrderVO();
    }
}
