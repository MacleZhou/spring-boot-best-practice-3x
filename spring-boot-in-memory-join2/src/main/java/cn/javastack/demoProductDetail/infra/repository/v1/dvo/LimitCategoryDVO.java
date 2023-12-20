package cn.javastack.demoProductDetail.infra.repository.v1.dvo;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalLimitCategory;
import lombok.Getter;

@Getter
public class LimitCategoryDVO {
    private String companyCode;
    private String code;
    private String shortName;
    private int ageFrom;
    private int ageTo;
    private String ageUnit;

    private LimitCategoryDVO(){}

    public static LimitCategoryDVO apply(MedicalLimitCategory medicalLimitCategory){
        LimitCategoryDVO categoryDVO = new LimitCategoryDVO();
        categoryDVO.companyCode = medicalLimitCategory.getCompanyCode();
        categoryDVO.code = medicalLimitCategory.getCode();
        categoryDVO.shortName = medicalLimitCategory.getShortName();
        categoryDVO.ageFrom = medicalLimitCategory.getAgeFrom();
        categoryDVO.ageTo = medicalLimitCategory.getAgeTo();
        categoryDVO.ageUnit = medicalLimitCategory.getAgeUnit();
        return categoryDVO;
    }
}
