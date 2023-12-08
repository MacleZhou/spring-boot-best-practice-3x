package cn.javastack.demoProductDetail.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalLimitCategory {
    private String code;
    private String shortName;
    private int valueType;
}
