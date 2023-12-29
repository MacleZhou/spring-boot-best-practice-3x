package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v4;

import cn.javastack.data.loader.aggregate.aimdemo.domain.model.entity.Product;
import cn.javastack.data.loader.aggregate.aimdemo.domain.service.ProductService;
import cn.javastack.data.loader.core.DataLoaderInMemoryService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImplV42 implements ProductService {


    @Autowired
    private DataLoaderInMemoryService dataLoaderInMemoryService;

    public Product loadProduct(String companyCode, String productCode) {
        AggregatedPOV42 aggregatedDVO = new AggregatedPOV42(companyCode, productCode);

        //this.aggregateService.aggregateInMemory(aggregatedDVO);
        this.dataLoaderInMemoryService.loaderInMemory(aggregatedDVO);

        log.info(JSON.toJSONString(aggregatedDVO));

        //11. 转换ProductDetailDVO到Product中返回
        return null;
    }

}
