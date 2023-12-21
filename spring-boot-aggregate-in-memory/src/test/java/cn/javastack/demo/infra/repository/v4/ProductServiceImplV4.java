package cn.javastack.demo.infra.repository.v4;

import cn.javastack.aggregateinmemory.core.AggregateService;
import cn.javastack.demo.domain.model.entity.Product;
import cn.javastack.demo.domain.service.ProductService;
import cn.javastack.demo.infra.repository.v4.dvo.AggregatedDVO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("productServiceImplV4")
public class ProductServiceImplV4 implements ProductService {

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
