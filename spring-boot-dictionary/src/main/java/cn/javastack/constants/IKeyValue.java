package cn.javastack.constants;

import java.util.List;

public interface IKeyValue<K, V> {

    default K key(V v){
        for (int i = 0; i < this.keyValues().size(); i++) {
            KeyValue<K, V> keyValue = keyValues().get(i);
            if(keyValue.getValue().equals(v)){
                return keyValue.getKey();
            }
        }
        return null;
    }

    default V value(K k){
        for (int i = 0; i < this.keyValues().size(); i++) {
            KeyValue<K, V> keyValue = keyValues().get(i);
            if(keyValue.getKey().equals(k)){
                return keyValue.getValue();
            }
        }
        return null;
    }

    List<KeyValue<K, V>> keyValues();
}