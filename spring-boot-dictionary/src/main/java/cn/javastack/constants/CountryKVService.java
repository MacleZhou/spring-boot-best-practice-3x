package cn.javastack.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("countryKVService")
@Constant("country")
public class CountryKVService implements IKeyValue<Integer, String>{

    //Simulate the data in DB
    private Map<Integer, String> keyValues;
    @PostConstruct
    public void init(){
        keyValues = new HashMap<>();
        keyValues.put(86, "China");
        keyValues.put(16, "Malaysia");
    }

    @Override
    public List<KeyValue<Integer, String>> keyValues() {
        List<KeyValue<Integer, String>> keyValues1 = new ArrayList<>();
        keyValues.forEach((k,v)->{
            keyValues1.add(new KeyValue<>(k, v));
        });
        return keyValues1;
    }
}
