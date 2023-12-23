package cn.javastack.constants;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component("constantService")
public class ConstantService<K, V> implements BeanFactoryAware {
    private BeanFactory beanFactory;

    private ConcurrentMap<String, List<KeyValue<K, V>>> CONSTANT_CACHE = new ConcurrentHashMap<>();

    private ConcurrentMap<String, Method> CONSTANT_PROVIDER_METHOD = new ConcurrentHashMap<>();

    public List<KeyValue<K, V>> get(String type, Map queryVariables){
        if(CONSTANT_CACHE.containsKey(type)){
            return CONSTANT_CACHE.get(type);
        }
        else if(CONSTANT_PROVIDER_METHOD.containsKey(type)){
            Method method = CONSTANT_PROVIDER_METHOD.get(type);
            ConstantKV constantKV = method.getAnnotation(ConstantKV.class);
            Object springBean = beanFactory.getBean(method.getDeclaringClass());
            try {
                Object returnObject = null;
                //Service 方法没有参数
                if(0 == method.getParameterCount()){
                    returnObject = method.invoke(springBean, null);
                } else {
                    //创建nameDiscovery, 获取方法参数名（method.getParameters()方法获取到的参数名为arg0等)
                    ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
                    String[] methodParameterNames = nameDiscoverer.getParameterNames(method);
                    Parameter[] parameterInfoArray = method.getParameters();
                    Object[] parameters = new Object[method.getParameterCount()];
                    for (int i = 0; i < method.getParameterCount(); i++) {
                        String parameterName = methodParameterNames[i];
                        Class parameterType = parameterInfoArray[i].getType();
                        Object param = queryVariables.get(parameterName);
                        //TODO parameters[i] = DefaultConverter.cast(param, parameterType);
                        parameters[i] = param;
                    }
                    returnObject = method.invoke(springBean, parameters);
                }

                List<KeyValue<K,V>> keyValues = null;
                if(returnObject != null){
                    keyValues = new ArrayList<>();
                    Field keyField = null;
                    Field valueField = null;
                    if(Collection.class.isAssignableFrom(returnObject.getClass())){
                        Collection<?> collectionReturnObjects = (Collection<?>) returnObject;
                        for(Object object : collectionReturnObjects){
                            keyValues.add(convertObject2KeyValue(constantKV, object, keyField, valueField));
                        }
                    } else {
                        keyValues.add(convertObject2KeyValue(constantKV, returnObject, keyField, valueField));
                    }

                    //有参数时不缓存
                    if(constantKV.cacheable() && method.getParameterCount() == 0 && !CollectionUtils.isEmpty(keyValues)){
                        CONSTANT_CACHE.put(constantKV.value(), keyValues);
                    }
                }
                return keyValues;
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public ConcurrentMap<String, List<KeyValue<K, V>>> findAll(){
        ConcurrentMap<String, List<KeyValue<K, V>>> all = new ConcurrentHashMap<>();
        all.putAll(CONSTANT_CACHE);
        return all;
    }

    public V getValue(String type, K key, Map queryVariables){
        List<KeyValue<K, V>> keyValues = this.get(type, queryVariables);
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue<K, V> keyValue = keyValues.get(i);
            if(keyValue.getKey().equals(key)){
                return keyValue.getValue();
            }
        }
        return null;
    }

    public K getKey(String type, V value, Map queryVariables){
        List<KeyValue<K, V>> keyValues = this.get(type, queryVariables);
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
                    Reflections reflections = new Reflections("cn.*", Arrays.asList(new MethodParameterScanner(), new MethodParameterNamesScanner(), new SubTypesScanner(), new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
                    reflections.getTypesAnnotatedWith(ConstantKV.class)
                        .forEach(clz -> {
                            try {
                                log.debug(clz.getCanonicalName() + ": is enum={}, IKeyService={}", clz.isEnum());
                                ConstantKV cEnum = clz.getAnnotation(ConstantKV.class);
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
                                }
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        });
                    reflections.getMethodsAnnotatedWith(ConstantKV.class).forEach(method -> {
                        ConstantKV constantKV = method.getAnnotation(ConstantKV.class);
                        CONSTANT_PROVIDER_METHOD.put(constantKV.value(), method);
                    });
                }
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private KeyValue convertObject2KeyValue(ConstantKV constantKV, Object object, Field keyField, Field valueField){
        if(object instanceof KeyValue<?,?>){
            return (KeyValue) object;
        } else {
            try {
                if(keyField == null){
                    if(StringUtils.isBlank(constantKV.k())){
                        throw new RuntimeException("NOT DEFINE THE KEY FIELD");
                    }
                    keyField = object.getClass().getDeclaredField(constantKV.k());
                    keyField.setAccessible(true);
                    valueField = keyField;
                    if(StringUtils.isNoneBlank(constantKV.v())){
                        valueField = object.getClass().getDeclaredField(constantKV.v());
                        valueField.setAccessible(true);
                    }
                }
                return new KeyValue(keyField.get(object), valueField.get(object));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
