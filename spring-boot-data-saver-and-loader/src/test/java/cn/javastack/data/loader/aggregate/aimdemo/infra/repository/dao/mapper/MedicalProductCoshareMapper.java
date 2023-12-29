package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.mapper;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProductCoshare;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MedicalProductCoshare
 * companyCode, productCode,   role,   category,    amount
 * 016,         H1N100N6,      I,      LL,          1000
 * 016,         H1N100N6,      I,      AL,          100
 * 016,         H1N100N6,      I,      DL,          10
 * */
@Service("medicalProductCoshareMapper")
public class MedicalProductCoshareMapper {
    private Map<String, List<MedicalProductCoshare>> data = new HashMap<>();

    @PostConstruct
    public void init(){
        List<MedicalProductCoshare> medicalProductLimits = new ArrayList<>();
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "LL", 1000));
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "AL", 100));
        medicalProductLimits.add(createData("016", "H1N100N6", "I", "DL", 10));

        data.put("H1N100N6", medicalProductLimits);
    }

    private static MedicalProductCoshare createData(String companyCode, String productCode, String role, String category, int amount) {
        MedicalProductCoshare medicalProductLimit = new MedicalProductCoshare();
        medicalProductLimit.setCompanyCode(companyCode);
        medicalProductLimit.setProductCode(productCode);
        medicalProductLimit.setRole(role);
        medicalProductLimit.setCategory(category);
        medicalProductLimit.setAmount(amount);
        return medicalProductLimit;
    }

    public List<MedicalProductCoshare> get(String productCode){
        return data.get(productCode);
    }
}
