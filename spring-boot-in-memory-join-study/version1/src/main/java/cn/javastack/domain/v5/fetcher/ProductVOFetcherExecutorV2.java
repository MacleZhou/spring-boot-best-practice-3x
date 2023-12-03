package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.entity.Product;
import cn.javastack.domain.repository.ProductRepository;
import cn.javastack.domain.model.vo.ProductVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductVOFetcherExecutorV2
        extends BaseItemFetcherExecutor<ProductVOFetcherV2, Product, ProductVO> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    protected Long getFetchId(ProductVOFetcherV2 fetcher) {
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
    protected void setResult(ProductVOFetcherV2 fetcher, List<ProductVO> productVOs) {
        if (CollectionUtils.isNotEmpty(productVOs)) {
            fetcher.setProduct(productVOs.get(0));
        }
    }

    @Override
    public boolean support(Class<ProductVOFetcherV2> cls) {
        // 暂时忽略，稍后会细讲
        return ProductVOFetcherV2.class.isAssignableFrom(cls);
    }
}