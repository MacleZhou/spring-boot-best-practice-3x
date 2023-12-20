package cn.javastack.demoProductDetail.infra.repository.v6.dvo;


import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductLimit;
import lombok.Getter;

@Getter
public class ProductLimitDVO {
    private String companyCode;
    private String productCode;
    private String role;
    private String category;
    private int amount;

    private ProductLimitDVO(){}

    public static ProductLimitDVO apply(MedicalProductLimit medicalProductLimit){
        ProductLimitDVO productLimitDVO = new ProductLimitDVO();
        productLimitDVO.companyCode = medicalProductLimit.getCompanyCode();
        productLimitDVO.amount = medicalProductLimit.getAmount();
        productLimitDVO.productCode = medicalProductLimit.getProductCode();
        productLimitDVO.role = medicalProductLimit.getRole();
        productLimitDVO.category = medicalProductLimit.getCategory();
        return productLimitDVO;
    }
}
