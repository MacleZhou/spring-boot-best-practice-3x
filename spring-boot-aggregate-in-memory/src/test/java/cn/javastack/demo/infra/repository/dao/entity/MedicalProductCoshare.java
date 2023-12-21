package cn.javastack.demo.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalProductCoshare {
    private String companyCode;
    private String productCode;
    private String role;
    private String category;
    private int amount;
}
