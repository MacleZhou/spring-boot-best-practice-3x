package cn.javastack.demo.infra.repository.v1.dvo;

import cn.javastack.demo.infra.repository.dvo.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AggregatedDVO {

    private final String companyCode;

    private final String productCode;


    private ProductDVO product;

    private List<ProductLimitDVO> productLimits;

    private List<ProductDeductDVO> productDeducts;

    private List<ProductCoshareDVO> productCoshares;

    private List<ProductBenefitDVO> productBenefits;

    private Map<String, BenefitDVO> benefits;

    private Map<String, LimitCategoryDVO> limitCategories;

    private Map<String, List<ProductBenefitLimitDVO>> benefitLimits;

    public AggregatedDVO(String companyCode, String productCode){
        this.companyCode = companyCode;
        this.productCode = productCode;
    }
}
