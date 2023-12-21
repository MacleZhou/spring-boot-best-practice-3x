package cn.javastack.aimdemo.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalProductDeduct {
    private String companyCode;
    private String productCode;
    private String role;
    private String category;
    private int amount;
}
