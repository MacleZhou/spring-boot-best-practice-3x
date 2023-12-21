package cn.javastack.aimdemo.domain.service;

import cn.javastack.aimdemo.domain.model.entity.Product;

public interface ProductService {
    Product loadProduct(String companyCode, String productCode);
}
