package cn.javastack.demo.domain.service;

import cn.javastack.demo.domain.model.entity.Product;

public interface ProductService {
    Product loadProduct(String companyCode, String productCode);
}
