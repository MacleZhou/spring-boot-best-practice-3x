package cn.javastack.infra.utils;

import cn.javastack.constants.IKeyValue;
import cn.javastack.constants.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static <K, V> List<KeyValue<K, V>> converts(IKeyValue<K, V>... iKeyValues){
        List<KeyValue<K, V>> keyValues = new ArrayList<>();
        for (int i = 0; i < iKeyValues.length; i++) {
            IKeyValue genderEnum = iKeyValues[i];
            KeyValue<K, V> keyValue = new KeyValue(genderEnum.key(), genderEnum.value());
            keyValues.add(keyValue);
        }
        return keyValues;
    }
}
