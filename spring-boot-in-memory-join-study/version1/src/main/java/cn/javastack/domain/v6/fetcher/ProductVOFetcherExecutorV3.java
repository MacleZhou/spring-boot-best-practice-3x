package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.entity.Product;
import cn.javastack.domain.model.vo.ProductVO;
import cn.javastack.domain.repository.ProductRepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductVOFetcherExecutorV3
        extends BaseItemFetcherExecutor<ProductVOFetcherV3, Product, ProductVO> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    protected Long getFetchId(ProductVOFetcherV3 fetcher) {
        return fetcher.getProductId();
    }

    @Override
    protected List<Product> loadData(List<Long> ids) {
        return this.productRepository.getByIds(ids);
    }

    @Override
    protected Long getDataId(Product product) {
        return product.getId();
    }

    @Override
    protected ProductVO convertToVo(Product product) {
        return ProductVO.apply(product);
    }

    @Override
    protected void setResult(ProductVOFetcherV3 fetcher, List<ProductVO> productVOs) {
        if (CollectionUtils.isNotEmpty(productVOs)) {
            fetcher.setProduct(productVOs.get(0));
        }
    }

    @Override
    public boolean support(Class<ProductVOFetcherV3> cls) {
        // 暂时忽略，稍后会细讲
        return ProductVOFetcherV3.class.isAssignableFrom(cls);
    }
}