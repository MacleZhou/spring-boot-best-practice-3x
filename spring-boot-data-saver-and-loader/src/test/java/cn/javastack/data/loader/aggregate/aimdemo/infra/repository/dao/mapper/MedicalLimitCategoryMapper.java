package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.mapper;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.dao.entity.MedicalLimitCategory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.javastack.data.loader.join.jimdemo.utils.TimeUtils.sleepAsMS;

/**
 * MedicalLimitCategory
 * companyCode, code,   shortName,       ageFrom,    ageTo,     ageUnit
 * 016,         LL,     Lifetime Limit,  16,         65,        YEAR
 * 016,         AL,     Annual Limit,    16,         65,        YEAR
 * 016,         DL,     Disability Limit,16,         65,        YEAR
 * 016,         VL,     Visit Limit,     16,         65,        YEAR
 * **/

@Service("medicalLimitCategoryMapper")
public class MedicalLimitCategoryMapper {
    private Map<String, MedicalLimitCategory> data = new HashMap<>();

    @PostConstruct
    public void init(){
        data.put("LL", createData("LL", "Lifetime Limit"));
        data.put("AL", createData("AL", "Annual Limit"));
        data.put("DL", createData("DL", "Disability Limit"));
        data.put("VL", createData("VL", "Visit Limit"));
    }

    private static MedicalLimitCategory createData(String code, String shortName) {
        MedicalLimitCategory medicalProductLimit = new MedicalLimitCategory();
        medicalProductLimit.setCompanyCode("016");
        medicalProductLimit.setCode(code);
        medicalProductLimit.setShortName(shortName);
        medicalProductLimit.setAgeFrom(16);
        medicalProductLimit.setAgeTo(65);
        medicalProductLimit.setAgeUnit("YEAR");
        return medicalProductLimit;
    }


    public List<MedicalLimitCategory> get(Set<String> codes) {
        sleepAsMS(4);
        List<MedicalLimitCategory> medicalProductLimits = new ArrayList<>();
        data.forEach((k, v) -> {
            if(codes.contains(k)){
                medicalProductLimits.add(v);
            }
        });
        return medicalProductLimits;
    }

    public MedicalLimitCategory get(String code) {
        return data.get(code);
    }
}
