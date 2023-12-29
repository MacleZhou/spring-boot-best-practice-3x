package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6.dvo;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.*;
import cn.javastack.data.loader.annotation.LoadDataToMemory;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AggregatedDVO {

    private final String companyCode;

    private final String productCode;

    @LoadDataToMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.ProductDVO).apply(#root)}"
    )
    private ProductDVO product;

    @LoadDataToMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductLimitMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.ProductLimitDVO).apply(#root)}"
    )
    private List<ProductLimitDVO> productLimits;

    @LoadDataToMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductDeductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.ProductDeductDVO).apply(#root)}"
    )
    private List<ProductDeductDVO> productDeducts;

    @LoadDataToMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductCoshareMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.ProductCoshareDVO).apply(#root)}"
    )
    private List<ProductCoshareDVO> productCoshares;

    @LoadDataToMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.ProductBenefitDVO).apply(#root)}"
    )
    private List<ProductBenefitDVO> productBenefits;


    @LoadDataToMemory(keyFromSourceData = "#{productBenefits.![code]}",
            loader = "#{@medicalBenefitMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.BenefitDVO).apply(#root)}",
            runLevel = 20
    )
    private Map<String, BenefitDVO> benefits;

    @LoadDataToMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitLimitMapper.getAll(#root)}",
            keyFromJoinData = "#{benefitCode}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.ProductBenefitLimitDVO).apply(#root)}",
            runLevel = 20
    )
    private Map<String, List<ProductBenefitLimitDVO>> benefitLimits;

    @LoadDataToMemory(keyFromSourceData = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6.support.CategoryCodeFetcher).fetch(#root)}",
            loader = "#{@medicalLimitCategoryMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dvo.LimitCategoryDVO).apply(#root)}",
            runLevel = 30
    )
    private Map<String, LimitCategoryDVO> limitCategories;

    public AggregatedDVO(String companyCode, String productCode){
        this.companyCode = companyCode;
        this.productCode = productCode;
    }
}
