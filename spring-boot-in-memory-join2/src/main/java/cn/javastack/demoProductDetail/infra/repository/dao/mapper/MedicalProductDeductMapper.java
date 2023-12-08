package cn.javastack.demoProductDetail.infra.repository.dao.mapper;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductDeduct;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("medicalProductDeductMapper")
public class MedicalProductDeductMapper {
    public List<MedicalProductDeduct> get(String companyCode, String productCode){
        List<MedicalProductDeduct> medicalProductLimits = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MedicalProductDeduct medicalProduct = new MedicalProductDeduct();
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
