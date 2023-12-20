package cn.javastack.demoProductDetail.infra.repository.v1.dvo;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductCoshare;
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
