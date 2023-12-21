package cn.javastack.demo.infra.repository.dvo;

import cn.javastack.demo.infra.repository.dao.entity.MedicalProductBenefitLimit;
import lombok.Getter;

@Getter
public class ProductBenefitLimitDVO {
    private String companyCode;
    private String productCodee;

    private String benefitCode;

    private String role;
    private String category;
    private String valueType;
    private int amount;

    private ProductBenefitLimitDVO(){}

    public static ProductBenefitLimitDVO apply(MedicalProductBenefitLimit medicalProductBenefitLimit){
        ProductBenefitLimitDVO productBenefitLimitDVO = new ProductBenefitLimitDVO();
        productBenefitLimitDVO.companyCode = medicalProductBenefitLimit.getCompanyCode();
        productBenefitLimitDVO.productCodee = medicalProductBenefitLimit.getProductCodee();
        productBenefitLimitDVO.benefitCode = medicalProductBenefitLimit.getBenefitCode();
        productBenefitLimitDVO.role = medicalProductBenefitLimit.getRole();
        productBenefitLimitDVO.category = medicalProductBenefitLimit.getCategory();
        productBenefitLimitDVO.valueType = medicalProductBenefitLimit.getValueType();
        productBenefitLimitDVO.amount = medicalProductBenefitLimit.getAmount();
        return productBenefitLimitDVO;
    }
}
