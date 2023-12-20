package cn.javastack.demoProductDetail.infra.repository.v1.dvo;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalBenefit;
import lombok.Getter;

@Getter
public class BenefitDVO {
    private String companyCode;
    private String name;
    private String code;

    private BenefitDVO(){}

    public static BenefitDVO apply(MedicalBenefit medicalBenefit){
        BenefitDVO benefitDVO = new BenefitDVO();
        benefitDVO.companyCode = medicalBenefit.getCompanyCode();
        benefitDVO.code = medicalBenefit.getCode();
        benefitDVO.name = medicalBenefit.getName();
        return benefitDVO;
    }
}
