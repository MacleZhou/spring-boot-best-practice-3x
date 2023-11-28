package cn.javastack.service.order;

import cn.javastack.core.wide.WideItemData;
import cn.javastack.wide.WideOrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "OrderS")
@Table(name = "t_order_s")
public class Order implements WideItemData<WideOrderType, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long addressId;
    private Long productId;
    private String descr;

    @Override
    public WideOrderType getItemType() {
        return WideOrderType.ORDER;
    }

    @Override
    public Long getKey() {
        return id;
    }
}
