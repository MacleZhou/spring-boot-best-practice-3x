package cn.javastack.data.loader.aggregate.aimdemo.domain.service;

import cn.javastack.data.loader.aggregate.aimdemo.domain.model.entity.Product;

public interface ProductService {
    Product loadProduct(String companyCode, String productCode);
}
