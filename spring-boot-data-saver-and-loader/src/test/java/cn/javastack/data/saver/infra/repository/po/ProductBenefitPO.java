package cn.javastack.data.saver.infra.repository.po;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductBenefitPO {
    private String id;
    private String productId;
    private String code;
    private boolean enabledAnnualLimit;
    private boolean enabledLifeTimeLimit;
}
