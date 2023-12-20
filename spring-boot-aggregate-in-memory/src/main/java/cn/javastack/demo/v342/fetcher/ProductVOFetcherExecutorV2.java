package cn.javastack.demo.v342.fetcher;

import cn.javastack.demo.service.product.Product;
import cn.javastack.demo.service.product.ProductRepository;
import cn.javastack.demo.v342.core.BaseItemFetcherExecutor;
import cn.javastack.demo.vo.ProductVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductVOFetcherExecutorV2 extends BaseItemFetcherExecutor<OrderDetailVOFetcherV2, Product, ProductVO> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    protected Long getFetchId(OrderDetailVOFetcherV2 fetcher){
        return fetcher.getOrder().getProductId();
    }

    @Override
    protected List<Product> loadData(List<Long> ids){
        return this.productRepository.getByIds(ids);
    }

    @Override
    protected Long getDataId(Product product){
        return product.getId();
    }

    @Override
    protected ProductVO convertToVo(Product product){
        return ProductVO.apply(product);
    }

    @Override
    protected void setResult(OrderDetailVOFetcherV2 fetcher, List<ProductVO> productVOS){
        if (CollectionUtils.isNotEmpty(productVOS)) {
            fetcher.setProduct(productVOS.get(0));
        }
    }

    @Override
    public boolean support(Class<OrderDetailVOFetcherV2> cls) {
        return OrderDetailVOFetcherV2.class.isAssignableFrom(cls);
    }
}