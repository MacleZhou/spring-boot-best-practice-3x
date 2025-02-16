package com.macle.study.security.config;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
@RequiredArgsConstructor
public class RedisCache {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存基本对象，Integer, String和实体类等
     * @param key 缓存的健值
     * @param value 缓存的值
     * */
    public <T> void set(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void set(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public boolean expire(final String key, final long timeout, final TimeUnit timeUnit) {
        return expire(key, timeout, timeUnit);
    }

    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public <T> T get(final String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public boolean remove(final String key) {
        return redisTemplate.delete(key);
    }

    public long delete(final Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    public <T> long set(final String key, final List<T> dataList){
        Long count = redisTemplate.opsForSet().add(key, dataList);
        return count == null ? 0 : count;
    }

    public <T> List<T> getList(final String key) {
        return (List<T>) redisTemplate.opsForSet().members(key);
    }
/*
    public <T> BoundSetOperations<String, T> boundSetOps(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperations = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperations.add(it.next());
        }
        return setOperations;
    }

    public <T> Set<T> getSet(final String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    public <T> void set(final String key, final Map<String, T> dataMap){
        if(dataMap != null){
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    public <T> Map<String, T> getMap(final String key) {
        return (Map<String, T>) redisTemplate.opsForHash().entries(key);
    }*/
}
