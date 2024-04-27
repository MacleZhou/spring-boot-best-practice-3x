package cn.javastack.data.saver.infra.repository.v1;

import cn.javastack.data.saver.domain.model.Product;
import cn.javastack.data.saver.domain.service.IProductRepository;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductTable;
import cn.javastack.data.saver.infra.repository.dao.mapper.ProductBenefitMapper;
import cn.javastack.data.saver.infra.repository.dao.mapper.ProductLimitMapper;
import cn.javastack.data.saver.infra.repository.dao.mapper.ProductMapper;
import cn.javastack.data.saver.infra.repository.support.PO2EntityConvert;
import jakarta.annotation.Resource;

public class ProductRepositoryImpl implements IProductRepository {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductLimitMapper productLimitMapper;

    @Resource
    private ProductBenefitMapper productBenefitMapper;

    @Override
    public void saveProduct(Product product) {

        ProductSavePOV1 productSavePOV1 = new ProductSavePOV1();
        PO2EntityConvert.prepareEntity(product, productSavePOV1);

        //1. 类型转换 -
        ProductTable productTable = productSavePOV1.getProduct();

        //2. 获取外键，并设置外键（AggregateRoot对象不用定义不需要）

        //3. 调用插入(如果是自增ID，在插入后能返回自增的ID到实体对象中@TableId标注的字段中)
        productMapper.create(productTable);

        //处理子表1，
        //获取子表数据 - product.getProductLimits() = getChildrenByParent(Product product)
        productSavePOV1.getProductLimits().forEach(productLimitTable -> {

            //2. 获取外键，并设置外键(如果是自增ID，在插入后能返回自增的ID到实体对象中@TableId标注的字段中)
            String parentId = productTable.getId();
            productLimitTable.setProductId(parentId);

            //3. 调用插入
            String id1 = productLimitMapper.create(productLimitTable);
        });

        productSavePOV1.getProductBenefits().forEach(productBenefitTable -> {
            //2. 获取外键，并设置外键
            String parentId = productTable.getId();
            productBenefitTable.setProductId(parentId);

            //3. 调用插入
            String id1 = productBenefitMapper.create(productBenefitTable);
        });
    }
}
