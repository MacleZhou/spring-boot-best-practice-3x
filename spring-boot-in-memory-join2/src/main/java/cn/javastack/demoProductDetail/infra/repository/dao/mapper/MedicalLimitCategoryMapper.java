package cn.javastack.demoProductDetail.infra.repository.dao.mapper;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalLimitCategory;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("medicalLimitCategoryMapper")
public class MedicalLimitCategoryMapper {
    public List<MedicalLimitCategory> get(String companyCode, List<String> codes){
        List<MedicalLimitCategory> medicalProductLimits = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            MedicalLimitCategory medicalProduct = new MedicalLimitCategory();
            medicalProductLimits.add(medicalProduct);
            medicalProduct.setCode(codes.get(i));
            medicalProduct.setShortName("SN-" + codes.get(i));
            medicalProduct.setValueType(RandomUtils.nextBoolean()? 0 : 1);
        }
        return medicalProductLimits;
    }
}
