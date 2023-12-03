package cn.javastack.domain.v4.fetcher;

import cn.javastack.domain.model.vo.ProductVO;

public interface ProductVOFetcherV1 {
    Long getProductId();

    void setProduct(ProductVO product);
}
