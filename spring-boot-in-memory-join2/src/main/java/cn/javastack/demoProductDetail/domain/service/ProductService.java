package cn.javastack.demoProductDetail.domain.service;

import cn.javastack.demoProductDetail.domain.model.entity.Product;

public interface ProductService {
    Product loadProduct(String companyCode, String productCode);
}
