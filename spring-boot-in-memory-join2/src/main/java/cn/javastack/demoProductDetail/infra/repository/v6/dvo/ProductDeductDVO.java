package cn.javastack.demoProductDetail.infra.repository.v6.dvo;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductDeduct;
import lombok.Getter;

@Getter
public class ProductDeductDVO {
    private String companyCode;
    private String productCode;
    private String role;
    private String category;
    private int amount;

    private ProductDeductDVO(){}

    public static ProductDeductDVO apply(MedicalProductDeduct medicalProductDeduct){
        ProductDeductDVO productDeductDVO = new ProductDeductDVO();
        productDeductDVO.companyCode = medicalProductDeduct.getCompanyCode();
        productDeductDVO.amount = medicalProductDeduct.getAmount();
        productDeductDVO.productCode = medicalProductDeduct.getProductCode();
        productDeductDVO.role = medicalProductDeduct.getRole();
        productDeductDVO.category = medicalProductDeduct.getCategory();
        return productDeductDVO;
    }
}
