package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.mapper;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProductDeduct;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MedicalProductDeduct
 * companyCode, productCode,   role,   category,    amount
 * 016,         H1N100N6,      I,      LL,          10000
 * 016,         H1N100N6,      I,      AL,          1000
 * 016,         H1N100N6,      I,      DL,          100
 * */

@Service("medicalProductDeductMapper")
public class MedicalProductDeductMapper {
    private Map<String, List<MedicalProductDeduct>> data = new HashMap<>();

    @PostConstruct
    public void init(){
        List<MedicalProductDeduct> medicalProductLimits = new ArrayList<>();
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "LL", 10000));
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "AL", 1000));
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "DL", 100));

        data.put("H1N100N6", medicalProductLimits);
    }

    private static MedicalProductDeduct createData(String companyCode, String productCode, String role, String category, int amount) {
        MedicalProductDeduct medicalProductLimit = new MedicalProductDeduct();
        medicalProductLimit.setCompanyCode(companyCode);
        medicalProductLimit.setProductCode(productCode);
        medicalProductLimit.setRole(role);
        medicalProductLimit.setCategory(category);
        medicalProductLimit.setAmount(amount);
        return medicalProductLimit;
    }

    public List<MedicalProductDeduct> get(String productCode){
        return data.get(productCode);
    }
}
