package cn.javastack.demo.infra.repository.v4.dvo;

import cn.javastack.aggregateinmemory.annotation.AggregateInMemory;
import cn.javastack.demo.infra.repository.dvo.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AggregatedDVO {

    private final String companyCode;

    private final String productCode;

    @AggregateInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductMapper.get(#root)}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.ProductDVO).apply(#root)}"
    )
    private ProductDVO product;

    @AggregateInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductLimitMapper.get(#root)}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.ProductLimitDVO).apply(#root)}"
    )
    private List<ProductLimitDVO> productLimits;

    @AggregateInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductDeductMapper.get(#root)}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.ProductDeductDVO).apply(#root)}"
    )
    private List<ProductDeductDVO> productDeducts;

    @AggregateInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductCoshareMapper.get(#root)}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.ProductCoshareDVO).apply(#root)}"
    )
    private List<ProductCoshareDVO> productCoshares;

    @AggregateInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitMapper.get(#root)}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.ProductBenefitDVO).apply(#root)}"
    )
    private List<ProductBenefitDVO> productBenefits;


    @AggregateInMemory(keyFromSourceData = "#{productBenefits.![code]}",
            loader = "#{@medicalBenefitMapper.get(#root)}",
            keyToGroupbyData = "#{code}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.BenefitDVO).apply(#root)}"
    )
    private Map<String, BenefitDVO> benefits;

    @AggregateInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitLimitMapper.getAll(#root)}",
            keyToGroupbyData = "#{benefitCode}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.ProductBenefitLimitDVO).apply(#root)}"
    )
    private Map<String, List<ProductBenefitLimitDVO>> benefitLimits;

    @AggregateInMemory(keyFromSourceData = "#{T(cn.javastack.demo.infra.repository.v4.support.CategoryCodeFetcher).fetch(#root)}",
            loader = "#{@medicalLimitCategoryMapper.get(#root)}",
            keyToGroupbyData = "#{code}",
            joinDataConverter = "#{T(cn.javastack.demo.infra.repository.dvo.LimitCategoryDVO).apply(#root)}"
    )
    private Map<String, LimitCategoryDVO> limitCategories;



    public AggregatedDVO(String companyCode, String productCode){
        this.companyCode = companyCode;
        this.productCode = productCode;
    }
}
