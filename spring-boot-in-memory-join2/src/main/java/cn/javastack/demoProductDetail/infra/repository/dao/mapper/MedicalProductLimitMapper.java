package cn.javastack.demoProductDetail.infra.repository.dao.mapper;

import cn.javastack.demoProductDetail.infra.repository.dao.entity.MedicalProductLimit;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * companyCode, productCode,   role,   category,    amount
 * 016,         H1N100N6,      I,      LL,          200000
 * 016,         H1N100N6,      I,      AL,          20000
 * 016,         H1N100N6,      I,      DL,          2000
 *
 * */

@Component("medicalProductLimitMapper")
public class MedicalProductLimitMapper {
    private Map<String, List<MedicalProductLimit>> data = new HashMap<>();

    @PostConstruct
    public void init(){
        List<MedicalProductLimit> medicalProductLimits = new ArrayList<>();
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "LL", 200000));
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "AL", 20000));
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "DL", 2000));

        data.put("H1N100N6", medicalProductLimits);
    }

    public List<MedicalProductLimit> get(String productCode){
        return data.get(productCode);
    }

    private static MedicalProductLimit createData(String companyCode, String productCode, String role, String category, int amount) {
        MedicalProductLimit medicalProductLimit = new MedicalProductLimit();
        medicalProductLimit.setCompanyCode(companyCode);
        medicalProductLimit.setProductCode(productCode);
        medicalProductLimit.setRole(role);
        medicalProductLimit.setCategory(category);
        medicalProductLimit.setAmount(amount);
        return medicalProductLimit;
    }
}
