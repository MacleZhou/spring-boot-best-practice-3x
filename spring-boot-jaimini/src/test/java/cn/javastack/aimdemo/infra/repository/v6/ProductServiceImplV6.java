package cn.javastack.aimdemo.infra.repository.v6;

import cn.javastack.jaimini.aim.core.AggregateService;
import cn.javastack.aimdemo.domain.model.entity.Product;
import cn.javastack.aimdemo.domain.service.ProductService;
import cn.javastack.aimdemo.infra.repository.v6.dvo.AggregatedDVO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("productServiceImplV6")
public class ProductServiceImplV6 implements ProductService {

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
