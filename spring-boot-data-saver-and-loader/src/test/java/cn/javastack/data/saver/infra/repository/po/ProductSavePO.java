package cn.javastack.data.saver.infra.repository.po;

import cn.javastack.data.saver.infra.repository.dao.entity.ProductBenefitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductLimitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductTable;

import java.util.List;

public interface ProductSavePO {

    ProductTable getProduct();

    List<ProductLimitTable> getProductLimits();

    List<ProductBenefitTable> getProductBenefits();

    void setProduct(ProductTable product);

    void setProductLimits(List<ProductLimitTable> productLimits);

    void setProductBenefits(List<ProductBenefitTable> productBenefits);
}
