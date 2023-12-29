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
public class ProductServiceImplV4 implements ProductService {


    @Autowired
    private DataLoaderInMemoryService dataLoaderInMemoryService;

    public Product loadProduct(String companyCode, String productCode) {
        AggregatedPOV4 aggregatedPOV4 = new AggregatedPOV4(companyCode, productCode);

        //this.aggregateService.aggregateInMemory(aggregatedDVO);
        this.dataLoaderInMemoryService.loaderInMemory(aggregatedPOV4);

        log.info(JSON.toJSONString(aggregatedPOV4));

        //11. 转换ProductDetailDVO到Product中返回
        return null;
    }

}
