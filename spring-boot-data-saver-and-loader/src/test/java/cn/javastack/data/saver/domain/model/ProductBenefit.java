package cn.javastack.data.saver.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductBenefit {
    private String id;
    private String productId;
    private String code;
    private boolean enabledAnnualLimit;
    private boolean enabledLifeTimeLimit;
}
