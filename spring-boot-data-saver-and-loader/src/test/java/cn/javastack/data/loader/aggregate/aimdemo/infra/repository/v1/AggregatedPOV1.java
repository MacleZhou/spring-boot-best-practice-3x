package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v1;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AggregatedPOV1 extends AggregatedPO{


    private ProductDVO product;

    private List<ProductLimitDVO> productLimits;

    private List<ProductDeductDVO> productDeducts;

    private List<ProductCoshareDVO> productCoshares;

    private List<ProductBenefitDVO> productBenefits;

    private Map<String, BenefitDVO> benefits;

    private Map<String, LimitCategoryDVO> limitCategories;

    private Map<String, List<ProductBenefitLimitDVO>> benefitLimits;

    public AggregatedPOV1(String companyCode, String productCode){
        super(companyCode, productCode);
    }
}
