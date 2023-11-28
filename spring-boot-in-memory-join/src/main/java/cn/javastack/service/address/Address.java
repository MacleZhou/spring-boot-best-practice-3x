package cn.javastack.service.address;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AddressS")
@Table(name = "t_address_s")
public class Address implements WideItemData<WideOrderType, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String detail;

    @Override
    public WideOrderType getItemType() {
        return WideOrderType.ADDRESS;
    }

    @Override
    public Long getKey() {
        return id;
    }
}
