package cn.javastack.jimdemo.v341.fetcher;

import cn.javastack.jimdemo.vo.ProductVO;

public interface ProductVOFetcherV1 {
    Long getProductId();

    void setProduct(ProductVO product);
}
