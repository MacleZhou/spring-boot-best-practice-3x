package cn.javastack.data.saver.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class Product {
    private String id;
    private String code;
    private String name;

    private String companyCode;

    private List<ProductLimit> productLimits;

    private List<ProductBenefit> productBenefits;
}
