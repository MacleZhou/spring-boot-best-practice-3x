package cn.javastack.data.saver.infra.repository.v1;

import cn.javastack.data.saver.infra.repository.dao.entity.ProductBenefitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductLimitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductTable;
import cn.javastack.data.saver.infra.repository.po.ProductSavePO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class ProductSavePOV1 implements ProductSavePO {
    private ProductTable product;
    private List<ProductLimitTable> productLimits;
    private List<ProductBenefitTable> productBenefits;
}
