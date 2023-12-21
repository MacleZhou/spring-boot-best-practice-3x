package cn.javastack.aimdemo.infra.repository.dvo;

import cn.javastack.aimdemo.infra.repository.dao.entity.MedicalProductBenefit;
import lombok.Getter;

@Getter
public class ProductBenefitDVO {
    private String companyCode;

    private String productCode;

    private String code;

    private boolean availableAnnualLimit;

    private boolean availableLifetimeLimit;

    private boolean availableDisabilityLimit;

    private ProductBenefitDVO(){}

    public static ProductBenefitDVO apply(MedicalProductBenefit medicalProductCoshare){
        ProductBenefitDVO productBenefitDVO = new ProductBenefitDVO();
        productBenefitDVO.companyCode = medicalProductCoshare.getCompanyCode();
        productBenefitDVO.productCode = medicalProductCoshare.getProductCode();
        productBenefitDVO.code = medicalProductCoshare.getCode();
        productBenefitDVO.availableAnnualLimit = medicalProductCoshare.isAvailableAnnualLimit();
        productBenefitDVO.availableLifetimeLimit = medicalProductCoshare.isAvailableLifetimeLimit();
        productBenefitDVO.availableDisabilityLimit = medicalProductCoshare.isAvailableDisabilityLimit();
        return productBenefitDVO;
    }
}
