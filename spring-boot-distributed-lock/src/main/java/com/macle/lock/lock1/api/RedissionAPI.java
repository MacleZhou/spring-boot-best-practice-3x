package com.macle.lock.lock1.api;

import com.macle.lock.lock1.config.RedisService;
import jakarta.annotation.Resource;
import jodd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/redission")
@Slf4j
public class RedissionAPI {

    @Resource
    private RedisService redisService;

    private static final String STRING_KEY = "redis:string";
    private static final String LIST_KEY = "redis:list";
    private static final String SET_KEY = "redis:set";
    private static final String HASH_KEY = "redis:hash";


    @PostMapping("/redis1")
    public void redisStringTest() {
        log.info("Redis String 数据类型测试");
        redisService.setString(STRING_KEY, LocalDateTime.now().toString());
        String redisGetStringData = redisService.getString(STRING_KEY);
        log.info("Redis String Get：{}", redisGetStringData);
        boolean remove = redisService.remove(STRING_KEY);
        log.info("Redis String Remove：{}", remove);
        redisGetStringData = redisService.getString(STRING_KEY);
        log.info("Redis String Get After Delete：{}", redisGetStringData);
    }

    @PostMapping("/redis2")
    public void redisListTest() {
        // 填充数据
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(LocalDateTime.now().getNano());
            ThreadUtil.sleep(5);
        }

        boolean addItemResult = redisService.addToList(LIST_KEY, LocalDateTime.now().getNano());
        redisService.getFromList(LIST_KEY)
                .forEach(s -> log.info("Redis List Get After Add Item：{}", s));

        boolean addListDataResult = redisService.addToList(LIST_KEY, list);
        log.info("Redis List Add List：{}", addListDataResult);
        redisService.getFromList(LIST_KEY)
                .forEach(s -> log.info("Redis List Get After Add List：{}", s));

        redisService.getFromList(LIST_KEY, 0, 2)
                .forEach(s -> log.info("Redis List Get By Index：{}", s));

        log.info("Redis List Size Before Delete：{}", redisService.getFromList(LIST_KEY).size());
        redisService.removeFromList(LIST_KEY, 0);
        log.info("Redis List Size After Delete：{}", redisService.getFromList(LIST_KEY).size());

        boolean remove = redisService.remove(LIST_KEY);
        log.info("Redis List Remove：{}", remove);
    }


    public void redisSetTest() {
        log.info("Redis Set 数据类型测试");
        // 填充数据
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(LocalDateTime.now().getNano());
            ThreadUtil.sleep(5);
        }

        boolean addItemResult = redisService.addToSet(SET_KEY, LocalDateTime.now().getNano());
        log.info("Redis Set Add item：{}", addItemResult);
        redisService.getFromSet(SET_KEY)
                .forEach(s -> log.info("Redis Set Get After Add Item：{}", s));

        boolean addListDataResult = redisService.addToSet(SET_KEY, list);
        log.info("Redis Set Add List：{}", addListDataResult);
        redisService.getFromSet(SET_KEY)
                .forEach(s -> log.info("Redis Set Get After Add List：{}", s));

        log.info("Redis Set Size Before Delete：{}", redisService.getFromSet(SET_KEY).size());
        redisService.removeFromSet(SET_KEY, LocalDateTime.now().getNano());
        log.info("Redis Set Size After Delete：{}", redisService.getFromSet(SET_KEY).size());

        boolean remove = redisService.remove(SET_KEY);
        log.info("Redis Set Remove：{}", remove);
    }


    public void redisHashTest() {
        log.info("Redis Hash 数据类型测试");
        Integer key = LocalDateTime.now().getNano();

        boolean addItemResult = redisService
                .addToHash(HASH_KEY, key, LocalDateTime.now().toString());
        log.info("Redis Hash Add item：{}", addItemResult);
        redisService.getFromHash(HASH_KEY)
                .forEach((k, v) -> log.info("Redis Hash Get After Add Item：{} - {}", k, v.toString()));

        log.info("Redis Hash Get By Key：{}", redisService.getFromHash(HASH_KEY, key).toString());

        log.info("Redis Hash Size Before Delete：{}", redisService.getFromHash(HASH_KEY).size());
        redisService.removeFromHash(HASH_KEY, key);
        log.info("Redis Hash Size After Delete：{}", redisService.getFromHash(HASH_KEY).size());

        boolean remove = redisService.remove(HASH_KEY);
        log.info("Redis Hash Remove：{}", remove);
    }
}
