package cn.javastack.demo.v341.fetcher;

import cn.javastack.demo.vo.ProductVO;

public interface ProductVOFetcherV1 {
    Long getProductId();

    void setProduct(ProductVO product);
}
