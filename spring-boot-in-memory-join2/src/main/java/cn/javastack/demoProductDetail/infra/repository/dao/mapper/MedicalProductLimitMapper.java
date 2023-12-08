package cn.javastack.demoProductDetail.infra.repository.dao.mapper;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductLimit;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("medicalProductLimitMapper")
public class MedicalProductLimitMapper {
    public List<MedicalProductLimit> get(String companyCode, String productCode){
        List<MedicalProductLimit> medicalProductLimits = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MedicalProductLimit medicalProduct = new MedicalProductLimit();
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
