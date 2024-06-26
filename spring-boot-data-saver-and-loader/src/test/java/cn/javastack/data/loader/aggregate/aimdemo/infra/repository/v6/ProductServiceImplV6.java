package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6;

import cn.javastack.data.loader.aggregate.aimdemo.domain.model.entity.Product;
import cn.javastack.data.loader.aggregate.aimdemo.domain.service.ProductService;
import cn.javastack.data.loader.core.DataLoaderInMemoryService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImplV6 implements ProductService {

    @Autowired
    private DataLoaderInMemoryService dataLoaderInMemoryService;

    public Product loadProduct(String companyCode, String productCode) {
        AggregatedPOV6 aggregatedPOV6 = new AggregatedPOV6(companyCode, productCode);

        this.dataLoaderInMemoryService.loaderInMemory(aggregatedPOV6);

        log.info(JSON.toJSONString(aggregatedPOV6));

        //11. 转换ProductDetailDVO到Product中返回
        return null;
    }

}
