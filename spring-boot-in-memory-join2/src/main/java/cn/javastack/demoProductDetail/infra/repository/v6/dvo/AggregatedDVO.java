package cn.javastack.demoProductDetail.infra.repository.v6.dvo;

import cn.javastack.aggregateinmemory.annotation.AggregateInMemory;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AggregatedDVO {

    private final String companyCode;

    private final String productCode;

    @AggregateInMemory(keysFromSourceData = {"#{productCode}"},
            loader = "#{@medicalProductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.demoProductDetail.infra.repository.v6.dvo.ProductDVO).apply(#root)}"
    )
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
