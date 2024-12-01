package com.macle.study.jetcache.api;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.macle.study.jetcache.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("user2")
public class User2Controller {

    @CreateCache(name= "userCache:", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    private Cache<Long, Object> userCache;

    @GetMapping("get")
    public User get(Long id){
        if(userCache.get(id) != null){
            return (User) userCache.get(id);
        }
        User user = new User();
        user.setId(id);
        user.setName("用户both"+id);
        user.setAge(23);
        user.setSex(1);
        userCache.put(id, user);
        log.info("User2.get第一次获取数据，未走缓存："+id);
        return user;
    }

    @PostMapping("updateUser")
    public Boolean updateUser(@RequestBody User user){
        // TODO 更新数据库
        userCache.put(user.getId(), user);
        return true;
    }

    @PostMapping("deleteUser")
    public Boolean deleteUser(Long id){
        // TODO 从数据库删除
        userCache.remove(id);
        return true;
    }

}