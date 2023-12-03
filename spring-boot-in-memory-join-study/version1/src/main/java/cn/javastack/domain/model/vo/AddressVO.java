package cn.javastack.domain.model.vo;

import cn.javastack.domain.model.entity.Address;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressVO {
    private Long id;
    private Long userId;
    private String detail;

    private AddressVO(){}

    public static AddressVO apply(Address address){
        return new AddressVO();
    }
}
