package cn.javastack.demo;

import cn.javastack.constants.ConstantKV;
import cn.javastack.constants.KeyValue;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("countryKVService")
public class CountryKVService {

    //Simulate the data in DB
    private Map<Integer, String> keyValues;

    private Map<String, List<BizNote>> bizNotes;

    @PostConstruct
    public void init(){
        keyValues = new HashMap<>();
        keyValues.put(86, "China");
        keyValues.put(16, "Malaysia");

        bizNotes = new HashMap<>();
        bizNotes.put("A.X", new ArrayList<>());
        bizNotes.put("B.Y", new ArrayList<>());
        bizNotes.get("A.X").add(new BizNote("A.X", "AD1", "AD1N", "AD1NC"));
        bizNotes.get("A.X").add(new BizNote("A.X", "AD2", "AD2N", "AD2NC"));
        bizNotes.get("A.X").add(new BizNote("A.X", "AD3", "AD3N", "AD3NC"));

        bizNotes.get("B.Y").add(new BizNote("B.Y", "BD1", "BD1N", "BD1NC"));
        bizNotes.get("B.Y").add(new BizNote("B.Y", "BD2", "BD2N", "BD2NC"));
        bizNotes.get("B.Y").add(new BizNote("B.Y", "BD3", "BD3N", "BD3NC"));
        bizNotes.get("B.Y").add(new BizNote("B.Y", "BD4", "BD4N", "BD3NC"));
        bizNotes.get("B.Y").add(new BizNote("B.Y", "BD5", "BD5N", "BD3NC"));
    }

    @ConstantKV(value = "country", cacheable = true)
    public List<KeyValue<Integer, String>> countries() {
        List<KeyValue<Integer, String>> keyValues1 = new ArrayList<>();
        keyValues.forEach((k,v)->{
            keyValues1.add(new KeyValue<>(k, v));
        });
        return keyValues1;
    }

    @ConstantKV(value = "bizDepartment", k="departmentCode", v="departmentName")
    public List<BizNote> departments(String area, String company) {
        String key = area + "." + company;
        return bizNotes.get(key);
    }

    @ConstantKV(value = "bizDepartment2", k="departmentCode")
    public List<BizNote> departments2(String area, String company) {
        String key = area + "." + company;
        return bizNotes.get(key);
    }

    @Getter
    @AllArgsConstructor
    static class BizNote{
        private String companyCode;
        private String departmentCode;
        private String departmentName;
        private String comments;
    }
}
