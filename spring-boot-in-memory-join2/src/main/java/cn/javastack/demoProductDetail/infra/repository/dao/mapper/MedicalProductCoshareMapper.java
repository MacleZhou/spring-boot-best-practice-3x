package cn.javastack.demoProductDetail.infra.repository.dao.mapper;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductCoshare;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("medicalProductCoshareMapper")
public class MedicalProductCoshareMapper {
    public List<MedicalProductCoshare> get(String companyCode, String productCode){
        List<MedicalProductCoshare> medicalProductLimits = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MedicalProductCoshare medicalProduct = new MedicalProductCoshare();
            medicalProductLimits.add(medicalProduct);
            medicalProduct.setCompanyCode(companyCode);
            medicalProduct.setProductCode(productCode);
            medicalProduct.setRole("I");
            medicalProduct.setAmount(RandomUtils.nextInt(0, 10));
            medicalProduct.setCategory("" + medicalProduct.getAmount());
        }
        return medicalProductLimits;
    }
}
