package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public abstract class AggregatedPO {

    private final String companyCode;

    private final String productCode;


    public abstract ProductDVO getProduct();

    public abstract List<ProductLimitDVO> getProductLimits();

    public abstract List<ProductDeductDVO> getProductDeducts();

    public abstract List<ProductCoshareDVO> getProductCoshares();

    public abstract List<ProductBenefitDVO> getProductBenefits();

    public abstract Map<String, BenefitDVO> getBenefits();

    public abstract Map<String, LimitCategoryDVO> getLimitCategories();

    public abstract Map<String, List<ProductBenefitLimitDVO>> getBenefitLimits();

    public AggregatedPO(String companyCode, String productCode){
        this.companyCode = companyCode;
        this.productCode = productCode;
    }
}
