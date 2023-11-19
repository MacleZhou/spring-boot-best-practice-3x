package cn.javastack.application;

import cn.javastack.constants.Constant;
import cn.javastack.constants.IKeyValue;
import cn.javastack.constants.KeyValue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component("constantService")
public class ConstantService<K, V> implements BeanFactoryAware {
    private BeanFactory beanFactory;

    private ConcurrentMap<String, List<KeyValue<K, V>>> CONSTANT_CACHE = new ConcurrentHashMap<>();

    private ConcurrentMap<String, Class> CONSTANT_BEANNAME = new ConcurrentHashMap<>();

    public List<KeyValue<K, V>> get(String type){
        if(CONSTANT_CACHE.containsKey(type)){
            return CONSTANT_CACHE.get(type);
        }
        else if(CONSTANT_BEANNAME.containsKey(type)){
            IKeyValue iKeyValue = (IKeyValue) beanFactory.getBean(CONSTANT_BEANNAME.get(type));
            return iKeyValue.keyValues();
        }
        return null;
    }

    public ConcurrentMap<String, List<KeyValue<K, V>>> findAll(){
        ConcurrentMap<String, List<KeyValue<K, V>>> all = new ConcurrentHashMap<>();
        all.putAll(CONSTANT_CACHE);
        CONSTANT_BEANNAME.forEach((k, v) -> {
            all.put(k, get(k));
        });
        return all;
    }

    public V getValue(String type, K key){
        List<KeyValue<K, V>> keyValues = this.get(type);
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue<K, V> keyValue = keyValues.get(i);
            if(keyValue.getKey().equals(key)){
                return keyValue.getValue();
            }
        }
        return null;
    }

    public K getKey(String type, V value){
        List<KeyValue<K, V>> keyValues = this.get(type);
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue<K, V> keyValue = keyValues.get(i);
            if(keyValue.getValue().equals(value)){
                return keyValue.getKey();
            }
        }
        return null;
    }

    @PostConstruct
    public void init(){
        Set<String> keys = CONSTANT_CACHE.keySet();
        if (keys.isEmpty()) {
            synchronized (CONSTANT_CACHE) {
                if (keys.isEmpty()) {
                    Reflections reflections = new Reflections("cn.*");
                    reflections.getTypesAnnotatedWith(Constant.class)
                        .forEach(clz -> {
                            try {
                                log.debug(clz.getCanonicalName() + ": is enum={}, IKeyService={}", clz.isEnum(),  clz.isAssignableFrom(IKeyValue.class));
                                Constant cEnum = clz.getAnnotation(Constant.class);
                                if (clz.isEnum()) {
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
                                    CONSTANT_CACHE.put(cEnum.value(), keyValues);
                                } else {
                                    Class<?>[] interfaces = clz.getInterfaces();
                                    for (int i = 0; i < interfaces.length; i++) {
                                        Class interf = interfaces[i];
                                        if(interf.getCanonicalName().equals(IKeyValue.class.getCanonicalName())){
                                            CONSTANT_BEANNAME.put(cEnum.value(), clz);
                                            break;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        });
                }
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
