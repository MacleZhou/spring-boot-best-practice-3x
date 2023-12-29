package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProduct;
import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProductDeduct;
import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProductLimit;
import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.*;
import cn.javastack.data.loader.annotation.DataLoaderInMemory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AggregatedPOV61{

    private final String companyCode;

    private final String productCode;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductMapper.get(#root)}"
    )
    private MedicalProduct product;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductLimitMapper.get(#root)}"
    )
    private List<MedicalProductLimit> productLimits;

    @DataLoaderInMemory(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductDeductMapper.get(#root)}"
    )
    private List<MedicalProductDeduct> productDeducts;

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

    @DataLoaderInMemory(keyFromSourceData = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6.CategoryCodeFetcherV61).fetch(#root)}",
            loader = "#{@medicalLimitCategoryMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.LimitCategoryDVO).apply(#root)}",
            runLevel = 30
    )
    private Map<String, LimitCategoryDVO> limitCategories;

    public AggregatedPOV61(String companyCode, String productCode){
        this.companyCode = companyCode;
        this.productCode = productCode;
    }
}
