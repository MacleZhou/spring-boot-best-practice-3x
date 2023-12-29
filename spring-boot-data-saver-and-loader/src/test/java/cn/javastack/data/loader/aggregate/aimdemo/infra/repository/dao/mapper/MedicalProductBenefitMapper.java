package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.mapper;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalProductBenefit;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * companyCode, productCode,   code,   availableAnnualLimit,    availableLifetimeLimit, availableDisabilityLimit
 * 016,         H1N100N6,      H01,    false,                   false,                  false
 * 016,         H1N100N6,      H02,    false,                   false,                  false
 * 016,         H1N100N6,      H03,    false,                   false,                  false
 * 016,         H1N100N6,      H04,    false,                   false,                  false
 * 016,         H1N100N6,      H05,    false,                   false,                  false
 * 016,         H1N100N6,      H06,    false,                   false,                  false
 * 016,         H1N100N6,      H07,    false,                   false,                  false
 * 016,         H1N100N6,      H08,    false,                   false,                  false
 * 016,         H1N100N6,      H09,    false,                   false,                  false
 * */

@Service("medicalProductBenefitMapper")
public class MedicalProductBenefitMapper {
    private Map<String, List<MedicalProductBenefit>> data = new HashMap<>();

    @PostConstruct
    public void init(){
        List<MedicalProductBenefit> medicalProductLimits = new ArrayList<>();
        medicalProductLimits.add(createData("016", "H1N100N6", "H01"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H02"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H03"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H04"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H05"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H06"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H07"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H08"));
        medicalProductLimits.add(createData("016", "H1N100N6", "H09"));

        data.put("H1N100N6", medicalProductLimits);
    }

    private static MedicalProductBenefit createData(String companyCode, String productCode, String benefitCode) {
        MedicalProductBenefit medicalProductLimit = new MedicalProductBenefit();
        medicalProductLimit.setCompanyCode(companyCode);
        medicalProductLimit.setProductCode(productCode);
        medicalProductLimit.setCode(benefitCode);
        return medicalProductLimit;
    }

    public List<MedicalProductBenefit> get(String productCode){
        return data.get(productCode);
    }
}
