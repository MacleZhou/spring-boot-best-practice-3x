package cn.javastack.demoProductDetail.domain.model.entity;

import lombok.Data;

@Data
public class BenefitLimit {
    private String role;
    private String category;
    private String categoryShortName;
    private int ageFrom;
    private int ageTo;
    private String ageUnit;
    private int valueType;
    private int amount;
}
