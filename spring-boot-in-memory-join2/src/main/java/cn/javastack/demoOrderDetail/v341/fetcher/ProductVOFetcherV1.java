package cn.javastack.demoOrderDetail.v341.fetcher;

import cn.javastack.demoOrderDetail.vo.ProductVO;

public interface ProductVOFetcherV1 {
    Long getProductId();

    void setProduct(ProductVO product);
}
