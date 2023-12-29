package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v1;

import cn.javastack.data.loader.aggregate.aimdemo.domain.model.entity.Product;
import cn.javastack.data.loader.aggregate.aimdemo.domain.service.ProductService;
import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.*;
import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.mapper.*;
import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.*;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ProductServiceImplV1 implements ProductService {

    @Resource
    private MedicalProductMapper medicalProductMapper;

    @Resource
    private MedicalProductLimitMapper medicalProductLimitMapper;

    @Resource
    private MedicalProductDeductMapper medicalProductDeductMapper;

    @Resource
    private MedicalProductCoshareMapper medicalProductCoshareMapper;

    @Resource
    private MedicalProductBenefitMapper medicalProductBenefitMapper;

    @Resource
    private MedicalProductBenefitLimitMapper medicalProductBenefitLimitMapper;

    @Resource
    private MedicalBenefitMapper medicalBenefitMapper;

    @Resource
    private MedicalLimitCategoryMapper medicalLimitCategoryMapper;

    public Product loadProduct(String companyCode, String productCode){
        AggregatedPOV1 aggregatedPOV1 = new AggregatedPOV1(companyCode, productCode);

        //1. 加载产品-(单体)(查询数据，模型转换(DATA->DVO)，设置结果)
        MedicalProduct medicalProduct = this.medicalProductMapper.get(productCode);
        ProductDVO productDVO = ProductDVO.apply(medicalProduct);
        aggregatedPOV1.setProduct(productDVO);

        //2. 加载产品限额-(集合)(查询数据，模型转换(DATA->DVO)，设置结果)
        List<MedicalProductLimit> medicalProductLimits = this.medicalProductLimitMapper.get(productCode);
        aggregatedPOV1.setProductLimits(medicalProductLimits.stream().map(medicalProductLimit -> {
            return ProductLimitDVO.apply(medicalProductLimit);
        }).collect(toList()));

        //3. 加载产品免赔
        List<MedicalProductDeduct> medicalProductDeducts = this.medicalProductDeductMapper.get(productCode);
        aggregatedPOV1.setProductDeducts(
                medicalProductDeducts.stream().map(medicalProductDeduct -> {return ProductDeductDVO.apply(medicalProductDeduct);
        }).collect(toList()));

        //4. 加载产品Coshare
        List<MedicalProductCoshare> medicalProductCoshares = this.medicalProductCoshareMapper.get(productCode);
        aggregatedPOV1.setProductCoshares(medicalProductCoshares.stream().map(medicalProductCoshare -> {return ProductCoshareDVO.apply(medicalProductCoshare);
        }).collect(toList()));

        //5. 加载产品责任
        List<MedicalProductBenefit> medicalProductBenefits = this.medicalProductBenefitMapper.get(productCode);
        aggregatedPOV1.setProductBenefits(medicalProductBenefits.stream().map(medicalProductBenefit -> {return ProductBenefitDVO.apply(medicalProductBenefit);}).collect(toList()));

        //6. 获取所有的责任码(SOURCE->KEY => Set<SOURCE_KEY>)
        List<String> benefitCodes = medicalProductBenefits.stream().map(medicalProductBenefit -> {return medicalProductBenefit.getCode();}).collect(toList());

        //7. 获取所有的分类码(多重获取SOURCE_KEY => Set<SOURCE_KEY>)
        Set<String> categoryCodes = medicalProductLimits.stream().map(medicalProductLimit -> {return medicalProductLimit.getCategory();}).collect(Collectors.toSet());
        categoryCodes.addAll(medicalProductDeducts.stream().map(medicalProductDeduct -> {return medicalProductDeduct.getCategory();}).collect(Collectors.toSet()));
        categoryCodes.addAll(medicalProductCoshares.stream().map(medicalProductCoshare -> {return medicalProductCoshare.getCategory();}).collect(Collectors.toSet()));

        //8. 循环加载每个责任的责任限定，同时把责任限定的分类码放入categoryCodes中(抽取责任码SOURCE_KEY -> 查询数据DATA -> 转换DVO -> 按责任分组(map中DATA_KEY-LIST<DVO>)-> 设置结果)
        Map<String, List<ProductBenefitLimitDVO>> benefitLimits = new HashMap<>();
        benefitCodes.stream().forEach(benfitCode -> {
            List<MedicalProductBenefitLimit> medicalProductBenefitLimits = this.medicalProductBenefitLimitMapper.get(productCode, benfitCode);
            if(!CollectionUtils.isEmpty(medicalProductBenefitLimits)) {
                benefitLimits.put(benfitCode, medicalProductBenefitLimits.stream().map(medicalProductBenefitLimit -> {
                    return ProductBenefitLimitDVO.apply(medicalProductBenefitLimit);
                }).collect(toList()));

                medicalProductBenefitLimits.stream().forEach(medicalProductBenefitLimit -> {
                    categoryCodes.add(medicalProductBenefitLimit.getCategory());
                });
            }
        });
        aggregatedPOV1.setBenefitLimits(benefitLimits);

        //9. 获取所有的责任定义(抽取责任码 -> 查询数据 -> 转换 -> 按责任分组(map中key-DVO)-> 设置结果)
        List<MedicalBenefit> medicalBenefits = this.medicalBenefitMapper.get(benefitCodes);
        Map<String, BenefitDVO> benefitDVOMap = new HashMap<>();
        medicalBenefits.stream().forEach(medicalBenefit -> {
            benefitDVOMap.put(medicalBenefit.getCode(), BenefitDVO.apply(medicalBenefit));
        });
        aggregatedPOV1.setBenefits(benefitDVOMap);

        //10. 获取所有的分类定义(抽取分类码 -> 查询数据 -> 转换 -> 按责任分组(map中DATA_KEY-DVO)-> 设置结果)
        List<MedicalLimitCategory> medicalLimitCategories = this.medicalLimitCategoryMapper.get(categoryCodes);
        Map<String, LimitCategoryDVO> limitCategoryDVOMap = new HashMap<>();
        medicalLimitCategories.forEach(medicalLimitCategory -> {
            limitCategoryDVOMap.put(medicalLimitCategory.getCode(), LimitCategoryDVO.apply(medicalLimitCategory));
        });
        aggregatedPOV1.setLimitCategories(limitCategoryDVOMap);

        log.info(JSON.toJSONString(aggregatedPOV1));

        //11. 转换ProductDetailDVO到Product中返回
        return null;
    }
}
