package cn.javastack.application;

import cn.javastack.constants.Constant;
import cn.javastack.constants.KeyValue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component("constantService")
public class ConstantService<K, V> {
    private ConcurrentMap<String, List<KeyValue<K, V>>> CNT_CACHE = new ConcurrentHashMap<>();

    public List<KeyValue<K, V>> get(String type){
        if(CNT_CACHE.containsKey(type)){
            return CNT_CACHE.get(type);
        }
        else{
            //search from DB
        }
        return null;
    }

    public ConcurrentMap<String, List<KeyValue<K, V>>> findAll(){
        return CNT_CACHE;
    }

    public V get(String type, K key){
        List<KeyValue<K, V>> keyValues = this.get(type);
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue<K, V> keyValue = keyValues.get(i);
            if(keyValue.getKey().equals(key)){
                return keyValue.getValue();
            }
        }
        return null;
    }

    @PostConstruct
    public void init(){
        Set<String> keys = CNT_CACHE.keySet();
        if (keys.isEmpty()) {
            synchronized (CNT_CACHE) {
                if (keys.isEmpty()) {
                    Reflections reflections = new Reflections("cn.*");
                    reflections.getTypesAnnotatedWith(Constant.class)
                        .forEach(clz -> {
                            try {
                                if (clz.isEnum()) {
                                    Constant cEnum = clz.getAnnotation(Constant.class);
                                    Object[] objects = clz.getEnumConstants();
                                    List<KeyValue<K, V>> keyValues = new ArrayList<>(objects.length);

                                    for (int i = 0; i < objects.length; i++) {
                                        Object object = objects[i];
                                        Enum<?> enumObj = (Enum<?>)object;
                                        Field[] fields = object.getClass().getDeclaredFields();
                                        Object keyObject = enumObj.name();
                                        Object valueObject = enumObj.name();
                                        int iKeyFieldIndex = -1;
                                        for (int j = 0; j < fields.length; j++) {
                                            if(j == fields.length - 1) break;
                                            Field field = fields[j];
                                            field.setAccessible(true);
                                            Object fieldValue = field.get(object);
                                            if(fieldValue.getClass().isEnum()){
                                                continue;
                                            }
                                            else if(iKeyFieldIndex < 0){
                                                keyObject = fieldValue;
                                                iKeyFieldIndex = j;
                                                continue;
                                            }
                                            if(iKeyFieldIndex > 0 && j < fields.length - 1){
                                                valueObject = fieldValue;
                                                break;
                                            }
                                        }
                                        KeyValue<K, V> keyValue = new KeyValue(keyObject, valueObject);
                                        keyValues.add(keyValue);
                                    }
                                    CNT_CACHE.put(cEnum.value(), keyValues);
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
