package cn.javastack.data.saver.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductBenefitTable extends BaseTable<String>{
    private String productId;
    private String code;
    private boolean enabledAnnualLimit;
    private boolean enabledLifeTimeLimit;
}
