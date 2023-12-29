package cn.javastack.aimdemo.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MedicalLimitCategory {
    private String companyCode;
    private String code;
    private String shortName;
    private int ageFrom;
    private int ageTo;
    private String ageUnit;
}
