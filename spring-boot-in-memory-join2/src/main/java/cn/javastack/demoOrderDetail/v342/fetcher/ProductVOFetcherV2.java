package cn.javastack.demoOrderDetail.v342.fetcher;

import cn.javastack.demoOrderDetail.vo.ProductVO;

public interface ProductVOFetcherV2 {
    Long getProductId();

    void setProduct(ProductVO product);
}
