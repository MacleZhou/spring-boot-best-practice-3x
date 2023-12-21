package cn.javastack.demo.infra.repository.dao.mapper;

import cn.javastack.demo.infra.repository.dao.entity.MedicalProduct;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("medicalProductMapper")
public class MedicalProductMapper {

    /**
     *companyCode, code,      name
     * 016,         H1N100N6,  UHXYN7 EXCELCARE UL
     * */

    private Map<String, MedicalProduct> data = new HashMap<>();

    @PostConstruct
    public void init(){
        MedicalProduct medicalProduct = new MedicalProduct();
        medicalProduct.setCompanyCode("016");
        medicalProduct.setCode("H1N100N6");
        medicalProduct.setName("UHXYN7 EXCELCARE UL");
        data.put("H1N100N6", medicalProduct);
    }

    public MedicalProduct get(String productCode){
        return data.get(productCode);
    }
}
