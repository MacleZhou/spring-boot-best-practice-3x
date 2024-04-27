package cn.javastack.data.saver.domain.service;

import cn.javastack.data.saver.domain.model.Product;

public interface IProductRepository {
    void saveProduct(Product product);
}
