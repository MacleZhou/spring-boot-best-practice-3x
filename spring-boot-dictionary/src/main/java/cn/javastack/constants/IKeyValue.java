package cn.javastack.constants;

import java.util.List;

public interface IKeyValue<K, V> {

    K key();

    V value();

    List<KeyValue<K, V>> keyValues();
}