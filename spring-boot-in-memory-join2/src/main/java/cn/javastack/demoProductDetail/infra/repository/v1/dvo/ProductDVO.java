package cn.javastack.demoProductDetail.infra.repository.v1.dvo;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProduct;
import lombok.Getter;

@Getter
public class ProductDVO {
    private String companyCode;
    private String code;
    private String name;

    private ProductDVO(){}

    public static ProductDVO apply(MedicalProduct medicalProduct){
        ProductDVO productDVO = new ProductDVO();
        productDVO.companyCode = medicalProduct.getCompanyCode();
        productDVO.code = medicalProduct.getCode();
        productDVO.name = medicalProduct.getName();
        return productDVO;
    }
}
