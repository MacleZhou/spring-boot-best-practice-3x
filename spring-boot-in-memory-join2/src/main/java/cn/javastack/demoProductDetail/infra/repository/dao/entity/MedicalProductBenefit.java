package cn.javastack.demoProductDetail.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalProductBenefit {
    private String companyCode;

    private String productCode;

    private String code;

    private boolean availableAnnualLimit;

    private boolean availableLifetimeLimit;

    private boolean availableDisabilityLimit;
}