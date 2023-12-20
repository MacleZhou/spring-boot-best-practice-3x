package cn.javastack.demoProductDetail.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalProductBenefitLimit {
    private String companyCode;
    private String productCodee;

    private String benefitCode;

    private String role;
    private String category;
    private String valueType;
    private int amount;
}
