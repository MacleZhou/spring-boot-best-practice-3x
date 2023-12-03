package cn.javastack.domain.model.vo;

import cn.javastack.domain.model.entity.PayInfo;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PayInfoVO {
    private Long id;
    private int amount;
    private PayInfoVO(){}

    public static PayInfoVO apply(PayInfo payInfo){
        return new PayInfoVO();
    }
}
