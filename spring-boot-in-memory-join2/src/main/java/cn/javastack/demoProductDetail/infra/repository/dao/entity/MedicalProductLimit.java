package cn.javastack.demoProductDetail.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalProductLimit {
    private String companyCode;
    private String productCode;
    private String role;
    private String category;
    private int amount;
}
