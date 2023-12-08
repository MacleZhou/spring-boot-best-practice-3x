package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.vo.ProductVO;

public interface ProductVOFetcherV3 extends ItemFetcher<ProductVO> {
    Long getProductId();

    void setProduct(ProductVO product);

    @Override
    default Long getFetchId() {
        return this.getProductId();
    }

    @Override
    default void setResult(ProductVO productVO){
        this.setProduct(productVO);
    }
}
