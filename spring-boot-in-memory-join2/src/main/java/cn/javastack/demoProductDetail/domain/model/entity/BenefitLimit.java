package cn.javastack.demoProductDetail.domain.model.entity;

import lombok.Data;

@Data
public class BenefitLimit {
    private String role;
    private String category;
    private int amount;
}
