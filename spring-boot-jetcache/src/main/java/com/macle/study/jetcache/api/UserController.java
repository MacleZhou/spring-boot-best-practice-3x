package com.macle.study.jetcache.api;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.macle.study.jetcache.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("getRemote")
    @Cached(name="userCache:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.REMOTE)
    public User getRemote(Long id){
        // 直接新建用户，模拟从数据库获取数据
        User user = new User();
        user.setId(id);
        user.setName("用户remote"+id);
        user.setAge(23);
        user.setSex(1);
        log.info("getRemote第一次获取数据，未走缓存："+id);
        return user;
    }

    @GetMapping("getLocal")
    @Cached(name="userCache:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.LOCAL)
    public User getLocal(Long id){
        // 直接新建用户，模拟从数据库获取数据
        User user = new User();
        user.setId(id);
        user.setName("用户local"+id);
        user.setAge(23);
        user.setSex(1);
        log.info("getLocal第一次获取数据，未走缓存："+id);
        return user;
    }

    @GetMapping("getBoth")
    @Cached(name="userCache:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public User getBoth(Long id){
        // 直接新建用户，模拟从数据库获取数据
        User user = new User();
        user.setId(id);
        user.setName("用户both"+id);
        user.setAge(23);
        user.setSex(1);
        log.info("getBoth第一次获取数据，未走缓存："+id);
        return user;
    }

    @PostMapping("updateUser")
    @CacheUpdate(name = "userCache:", key = "#user.id", value = "#user")
    public Boolean updateUser(@RequestBody User user){
        // TODO 更新数据库
        return true;
    }

    @PostMapping("deleteUser")
    @CacheInvalidate(name = "userCache:", key = "#id")
    public Boolean deleteUser(Long id){
        // TODO 从数据库删除
        return true;
    }

}