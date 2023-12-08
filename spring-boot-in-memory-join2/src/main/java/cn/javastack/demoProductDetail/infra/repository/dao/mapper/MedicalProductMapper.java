package cn.javastack.demoProductDetail.infra.repository.dao.mapper;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProduct;
import org.springframework.stereotype.Service;

@Service("medicalProductMapper")
public class MedicalProductMapper {
    public MedicalProduct get(String companyCode, String productCode){
        MedicalProduct medicalProduct = new MedicalProduct();
        medicalProduct.setCode(productCode);
        medicalProduct.setName("NAME IS " + productCode);
        return medicalProduct;
    }
}
