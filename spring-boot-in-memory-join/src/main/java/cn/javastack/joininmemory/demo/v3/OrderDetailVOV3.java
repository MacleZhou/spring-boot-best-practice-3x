package cn.javastack.joininmemory.demo.v3;

import cn.javastack.joininmemory.demo.*;
import lombok.Data;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
public class OrderDetailVOV3 extends OrderDetailVO {
    private final OrderVO order;
    private UserVO user;
    private AddressVO address;
    private ProductVO product;

}
