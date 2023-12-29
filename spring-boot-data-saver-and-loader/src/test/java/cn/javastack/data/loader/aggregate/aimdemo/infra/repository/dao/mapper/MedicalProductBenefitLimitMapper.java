package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.mapper;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProductBenefitLimit;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MedicalProductBenefitLimit
 * companyCode, productCode,   benefitCode,   role,    category,     valueType,    amount
 * 016,         H1N100N6,      H01,           A,       LL,           1,            20000
 * 016,         H1N100N6,      H01,           A,       DL,           1,            2000
 * 016,         H1N100N6,      H03,           A,       AL,           1,            30000
 * 016,         H1N100N6,      H03,           A,       DL,           1,            3000
 * 016,         H1N100N6,      H03,           A,       VL,           1,            300
 * */

@Service("medicalProductBenefitLimitMapper")
public class MedicalProductBenefitLimitMapper {

    private Map<String, Map<String, List<MedicalProductBenefitLimit>>> data = new HashMap<>();

    @PostConstruct
    public void init(){
        List<MedicalProductBenefitLimit> h01MedicalProductLimits = new ArrayList<>();
        h01MedicalProductLimits.add(createData("H1N100N6", "H01", "LL", 20000));
        h01MedicalProductLimits.add(createData("H1N100N6", "H01", "DL", 2000));

        Map<String, List<MedicalProductBenefitLimit>> H1N100N6Limits = new HashMap<>();
        H1N100N6Limits.put("H01", h01MedicalProductLimits);

        List<MedicalProductBenefitLimit> h03MedicalProductLimits = new ArrayList<>();
        h03MedicalProductLimits.add(createData("H1N100N6", "H03", "AL", 30000));
        h03MedicalProductLimits.add(createData("H1N100N6", "H03", "DL", 3000));
        h03MedicalProductLimits.add(createData("H1N100N6", "H03", "VL", 300));
        H1N100N6Limits.put("H03", h03MedicalProductLimits);

        data.put("H1N100N6", H1N100N6Limits);
    }

    private static MedicalProductBenefitLimit createData(String productCode, String benefitCode, String category, int amount) {
        MedicalProductBenefitLimit medicalProductLimit = new MedicalProductBenefitLimit();
        medicalProductLimit.setCompanyCode("016");
        medicalProductLimit.setProductCodee(productCode);
        medicalProductLimit.setBenefitCode(benefitCode);
        medicalProductLimit.setRole("I");
        medicalProductLimit.setCategory(category);
        medicalProductLimit.setAmount(amount);
        medicalProductLimit.setValueType("1");
        return medicalProductLimit;
    }

    public List<MedicalProductBenefitLimit> getAll(String productCode){
        List<MedicalProductBenefitLimit> result = new ArrayList<>();
        data.forEach((k, v) -> {
            v.forEach((ik, iv) -> {
                result.addAll(iv);
            });
        });
        return result;
    }

    public List<MedicalProductBenefitLimit> get(String productCode, String benefitCode){
        return data.containsKey(productCode) ? data.get(productCode).get(benefitCode) : null;
    }
}
