package cn.javastack.wide;

import cn.javastack.annotation.wide.BindFrom;
import cn.javastack.core.wide.WideItemKey;
import cn.javastack.core.wide.support.BindFromBasedWide;
import cn.javastack.service.address.Address;
import cn.javastack.service.order.Order;
import cn.javastack.service.product.Product;
import cn.javastack.service.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.List;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Entity
@Table(name = "t_wide_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "wide_order")
public class WideOrder extends BindFromBasedWide<Long, WideOrderType> {
    @Id
    @org.springframework.data.annotation.Id
    private Long id;

    @BindFrom(sourceClass = Order.class, field = "userId")
    private Long userId;
    @BindFrom(sourceClass = Order.class, field = "addressId")
    private Long addressId;
    @BindFrom(sourceClass = Order.class, field = "productId")
    private Long productId;
    @BindFrom(sourceClass = Order.class, field = "descr")
    private String orderDescr;

    @BindFrom(sourceClass = User.class, field = "name")
    private String userName;

    @BindFrom(sourceClass = Address.class, field = "detail")
    private String addressDetail;

    @BindFrom(sourceClass = Product.class, field = "name")
    private String productName;
    @BindFrom(sourceClass = Product.class, field = "price")
    private Integer productPrice;

    public WideOrder(Long orderId){
        setId(orderId);
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isValidate() {
        return userId != null && addressId != null && productId != null;
    }

    @Override
    public List<WideItemKey> getItemsKeyByType(WideOrderType wideOrderType) {
        switch (wideOrderType){
            case ORDER:
                return Collections.singletonList(new WideItemKey(wideOrderType, getId()));
            case USER:
                return Collections.singletonList(new WideItemKey(wideOrderType, getUserId()));
            case ADDRESS:
                return Collections.singletonList(new WideItemKey(wideOrderType, getAddressId()));
            case PRODUCT:
                return Collections.singletonList(new WideItemKey(wideOrderType, getProductId()));
        }

        return Collections.emptyList();
    }
}
