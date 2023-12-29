package cn.javastack.aimdemo.infra.repository.v4.dvo;

import cn.javastack.aimdemo.infra.repository.dvo.*;
import cn.javastack.data.loader.annotation.LoadDataToField;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AggregatedDVO {

    private final String companyCode;

    private final String productCode;

    @LoadDataToField(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.ProductDVO).apply(#root)}"
    )
    private ProductDVO product;

    @LoadDataToField(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductLimitMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.ProductLimitDVO).apply(#root)}"
    )
    private List<ProductLimitDVO> productLimits;

    @LoadDataToField(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductDeductMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.ProductDeductDVO).apply(#root)}"
    )
    private List<ProductDeductDVO> productDeducts;

    @LoadDataToField(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductCoshareMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.ProductCoshareDVO).apply(#root)}"
    )
    private List<ProductCoshareDVO> productCoshares;

    @LoadDataToField(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitMapper.get(#root)}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.ProductBenefitDVO).apply(#root)}"
    )
    private List<ProductBenefitDVO> productBenefits;


    @LoadDataToField(keyFromSourceData = "#{productBenefits.![code]}",
            loader = "#{@medicalBenefitMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.BenefitDVO).apply(#root)}"
    )
    private Map<String, BenefitDVO> benefits;

    @LoadDataToField(keyFromSourceData = "#{productCode}",
            loader = "#{@medicalProductBenefitLimitMapper.getAll(#root)}",
            keyFromJoinData = "#{benefitCode}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.ProductBenefitLimitDVO).apply(#root)}"
    )
    private Map<String, List<ProductBenefitLimitDVO>> benefitLimits;

    @LoadDataToField(keyFromSourceData = "#{T(cn.javastack.aimdemo.infra.repository.v4.support.CategoryCodeFetcher).fetch(#root)}",
            loader = "#{@medicalLimitCategoryMapper.get(#root)}",
            keyFromJoinData = "#{code}",
            dataConverter = "#{T(cn.javastack.aimdemo.infra.repository.dvo.LimitCategoryDVO).apply(#root)}"
    )
    private Map<String, LimitCategoryDVO> limitCategories;



    public AggregatedDVO(String companyCode, String productCode){
        this.companyCode = companyCode;
        this.productCode = productCode;
    }
}
