package cn.javastack.demo.infra.repository.dvo;

import cn.javastack.demo.infra.repository.dao.entity.MedicalProductCoshare;
import lombok.Getter;

@Getter
public class ProductCoshareDVO {
    private String companyCode;
    private String productCode;
    private String role;
    private String category;
    private int amount;

    private ProductCoshareDVO(){}

    public static ProductCoshareDVO apply(MedicalProductCoshare medicalProductCoshare){
        ProductCoshareDVO productCoshareDVO = new ProductCoshareDVO();
        productCoshareDVO.companyCode = medicalProductCoshare.getCompanyCode();
        productCoshareDVO.amount = medicalProductCoshare.getAmount();
        productCoshareDVO.productCode = medicalProductCoshare.getProductCode();
        productCoshareDVO.role = medicalProductCoshare.getRole();
        productCoshareDVO.category = medicalProductCoshare.getCategory();
        return productCoshareDVO;
    }
}
