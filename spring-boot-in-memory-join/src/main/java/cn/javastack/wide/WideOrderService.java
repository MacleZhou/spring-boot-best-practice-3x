package cn.javastack.wide;

import cn.javastack.core.wide.WideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Service
public class WideOrderService {
    @Autowired
    private WideService<Long, WideOrderType> wideService;

    public void index(Long orderId){
        this.wideService.index(orderId);
    }

    public void updateOrder(Long orderId){
        this.wideService.updateItem(WideOrderType.ORDER, orderId);
    }

    public void updateUser(Long userId){
        this.wideService.updateItem(WideOrderType.USER, userId);
    }

    public void updateAddress(Long addressId){
        this.wideService.updateItem(WideOrderType.ADDRESS, addressId);
    }

    public void updateProduct(Long productId){
        this.wideService.updateItem(WideOrderType.PRODUCT, productId);
    }
}
