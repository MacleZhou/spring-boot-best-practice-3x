package cn.javastack.demoOrderDetail.vo;

import cn.javastack.demoOrderDetail.service.address.Address;
import lombok.Value;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Value
public class AddressVO {
    private Long id;
    private String detail;

    public static AddressVO apply(Address address){
        if (address == null){
            return null;
        }
        return new AddressVO(address.getId(), address.getDetail());
    }
}
