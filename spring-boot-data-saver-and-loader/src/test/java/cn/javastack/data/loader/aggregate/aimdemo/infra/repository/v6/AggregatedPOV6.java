package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.*;
import cn.javastack.data.loader.annotation.DataLoaderInMemory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AggregatedPOV6 extends AggregatedPO{

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.ProductDVO).apply(#root)}"
    )
    private ProductDVO product;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductLimitMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.ProductLimitDVO).apply(#root)}"
    )
    private List<ProductLimitDVO> productLimits;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductDeductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.ProductDeductDVO).apply(#root)}"
    )
    private List<ProductDeductDVO> productDeducts;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductCoshareMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.ProductCoshareDVO).apply(#root)}"
    )
    private List<ProductCoshareDVO> productCoshares;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.ProductBenefitDVO).apply(#root)}"
    )
    private List<ProductBenefitDVO> productBenefits;


    @DataLoaderInMemory(keyFromSourceData = "#{productBenefits.![code]}",
            loader = "#{@medicalBenefitMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.BenefitDVO).apply(#root)}",
            runLevel = 20
    )
    private Map<String, BenefitDVO> benefits;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitLimitMapper.getAll(#root)}",
            keyFromJoinData = "#{benefitCode}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.ProductBenefitLimitDVO).apply(#root)}",
            runLevel = 20
    )
    private Map<String, List<ProductBenefitLimitDVO>> benefitLimits;

    @DataLoaderInMemory(keyFromSourceData = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.support.CategoryCodeFetcher).fetch(#root)}",
            loader = "#{@medicalLimitCategoryMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.LimitCategoryDVO).apply(#root)}",
            runLevel = 30
    )
    private Map<String, LimitCategoryDVO> limitCategories;

    public AggregatedPOV6(String companyCode, String productCode){
        super(companyCode, productCode);
    }
}
