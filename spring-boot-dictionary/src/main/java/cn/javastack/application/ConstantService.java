package cn.javastack.application;

import cn.javastack.constants.Constant;
import cn.javastack.constants.IKeyValue;
import cn.javastack.constants.KeyValue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component("constantService")
public class ConstantService {
    private ConcurrentMap<String, List<KeyValue>> CNT_CACHE = new ConcurrentHashMap<>();

    public List<KeyValue> get(String type){
        if(CNT_CACHE.containsKey(type)){
            return CNT_CACHE.get(type);
        }
        else{
            //search from DB
        }
        return null;
    }

    public ConcurrentMap<String, List<KeyValue>> findAll(){
        return CNT_CACHE;
    }

    @PostConstruct
    public void init(){
        Set<String> keys = CNT_CACHE.keySet();
        if (keys.isEmpty()) {
            synchronized (CNT_CACHE) {
                if (keys.isEmpty()) {
                    //Package pkg = GenderEnum.class.getPackage();
                    Reflections reflections = new Reflections("cn.*");
                    reflections.getTypesAnnotatedWith(Constant.class)
                            .forEach(clz -> {
                                try {
                                    if (clz.isEnum()) {
                                        Object[] objects = clz.getEnumConstants();
                                        Constant cEnum = clz.getAnnotation(Constant.class);
                                        IKeyValue constant = ((IKeyValue) objects[0]);
                                        CNT_CACHE.put(cEnum.value(), constant.keyValues());
                                    }
                                } catch (Exception e) {
                                    log.error("", e);
                                }
                            });
                }
            }
        }
    }

}
