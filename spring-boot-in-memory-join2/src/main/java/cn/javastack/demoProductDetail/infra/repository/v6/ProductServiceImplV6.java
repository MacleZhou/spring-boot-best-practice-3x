package cn.javastack.demoProductDetail.infra.repository.v6;

import cn.javastack.aggregateinmemory.core.AggregateService;
import cn.javastack.demoProductDetail.domain.model.entity.Product;
import cn.javastack.demoProductDetail.domain.service.ProductService;
import cn.javastack.demoProductDetail.infra.repository.dao.mapper.*;
import cn.javastack.demoProductDetail.infra.repository.v6.dvo.AggregatedDVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("productServiceImplV6")
public class ProductServiceImplV6 implements ProductService {

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

    @Autowired
    private AggregateService aggregateService;

    public Product loadProduct(String companyCode, String productCode) {
        AggregatedDVO aggregatedDVO = new AggregatedDVO(companyCode, productCode);

        this.aggregateService.aggregateInMemory(aggregatedDVO);

        log.info(JSON.toJSONString(aggregatedDVO));

        //11. 转换ProductDetailDVO到Product中返回
        return null;
    }

}
