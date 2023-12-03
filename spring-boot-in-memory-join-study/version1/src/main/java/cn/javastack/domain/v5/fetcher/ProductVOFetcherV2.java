package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.vo.ProductVO;

public interface ProductVOFetcherV2 extends ItemFetcher<ProductVO> {
    Long getProductId();

    void setProduct(ProductVO product);
}
