package cn.javastack.aimdemo.infra.repository.dao.mapper;

import cn.javastack.aimdemo.infra.repository.dao.entity.MedicalBenefit;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.javastack.jimdemo.utils.TimeUtils.sleepAsMS;

/**
 * MedicalBenefit
 * companyCode, code,   name
 * 016,         H01,    Room & Board
 * 016,         H02,    Call Service
 * 016,         H03,    Hospital Service
 * 016,         H04,    Surgical Fee
 * 016,         H05,    MM Call Service
 * 016,         H06,    Treatment Benefits
 * 016,         H07,    Anesthesia Amount
 * 016,         H08,    Operation Theater Fee
 * 016,         H09,    EOPT(H) Acc
 * **/

@Service("medicalBenefitMapper")
public class MedicalBenefitMapper {
    private Map<String, MedicalBenefit> data = new HashMap<>();

    @PostConstruct
    public void init(){
        data.put("H01", createData("H01", "Room & Board"));
        data.put("H02", createData("H02", "Call Service"));
        data.put("H03", createData("H03", "Hospital Service"));
        data.put("H04", createData("H04", "Surgical Fee"));
        data.put("H05", createData("H05", "MM Call Service"));
        data.put("H06", createData("H06", "Treatment Benefits"));
        data.put("H07", createData("H07", "Anesthesia Amount"));
        data.put("H08", createData("H08", "Operation Theater Fee"));
        data.put("H09", createData("H09", "EOPT(H) Acc"));
    }

    private static MedicalBenefit createData(String code, String shortName) {
        MedicalBenefit medicalProductLimit = new MedicalBenefit();
        medicalProductLimit.setCompanyCode("016");
        medicalProductLimit.setCode(code);
        medicalProductLimit.setName(shortName);
        return medicalProductLimit;
    }


    public List<MedicalBenefit> get(List<String> codes) {
        sleepAsMS(9);
        List<MedicalBenefit> medicalProductLimits = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            medicalProductLimits.add(this.get(codes.get(i)));
        }
        return medicalProductLimits;
    }

    public MedicalBenefit get(String code) {
        sleepAsMS(1);
        MedicalBenefit medicalProduct = new MedicalBenefit();
        medicalProduct.setCode(code);
        medicalProduct.setName("SN-" + code);
        medicalProduct.setCompanyCode("016");
        return medicalProduct;
    }
}
