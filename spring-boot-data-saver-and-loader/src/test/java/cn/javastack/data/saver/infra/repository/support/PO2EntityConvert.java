package cn.javastack.data.saver.infra.repository.support;

import cn.javastack.data.saver.domain.model.Product;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductBenefitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductLimitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductTable;
import cn.javastack.data.saver.infra.repository.po.ProductSavePO;

import java.util.ArrayList;
import java.util.List;

public class PO2EntityConvert<T extends ProductSavePO> {

    public static <T extends ProductSavePO> T prepareEntity(Product product, T productSaveVO){
        ProductTable productTable = new ProductTable();
        productSaveVO.setProduct(productTable);
        productTable.setCode(product.getCode());
        productTable.setName(product.getName());
        productTable.setCompanyCode(product.getCompanyCode());
        //设置主键, 如果没有定义，则是自定义主键, 则不需要这一步，会在“1. 类型转换"中自动完成。如果是自增主键，则不用此步骤
        productTable.setId(product.getCompanyCode() + "." + product.getCode());

        List<ProductLimitTable> productLimitTableList = new ArrayList<>();
        productSaveVO.setProductLimits(productLimitTableList);
        product.getProductLimits().forEach(productLimit -> {
            ProductLimitTable productLimitTable = new ProductLimitTable();
            productLimitTableList.add(productLimitTable);
            productLimitTable.setId(productLimit.getId());
            productLimitTable.setCoveredRole("I");
            productLimitTable.setCategory("LL");
            productLimitTable.setAmount(100);
            //productId是外键，此时可以不设置，通过框架或取并填入
        });

        List<ProductBenefitTable> productBenefitTables = new ArrayList<>();
        productSaveVO.setProductBenefits(productBenefitTables);
        product.getProductBenefits().forEach(productBenefit -> {
            //1. 类型转换
            ProductBenefitTable productBenefitTable = new ProductBenefitTable();
            productBenefitTables.add(productBenefitTable);
            productBenefitTable.setCode(productBenefit.getCode());
            productBenefitTable.setId(productBenefit.getId());
            //productId是外键，此时可以不设置，通过框架或取并填入
        });
        return productSaveVO;
    }
}
