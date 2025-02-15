package com.macle.lock.lock1.api;

import com.macle.lock.lock1.annotation.DistributedLock;
import com.macle.lock.lock1.api.res.R;
import com.macle.lock.lock1.api.res.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("lock1")
public class DistributedLockController {

    /**
     * 测试:lockKey动态值【SpEL表达式】 + 常量
     */
    @DistributedLock(lockKey = "'userId-' + #userId", expireTime = 100)
    @GetMapping("/lock-key-path-variable/{userId}")
    public R lock2(@PathVariable("userId") Long userId) {
        for (int i = 0; i < 1000000; i++) {
            log.info("waiting......");
        }
        return R.success();
    }


    /**
     * 测试:lockKey动态值【SpEL表达式】 + 常量
     */
    @DistributedLock(lockKey = "'userId-' + #user.id", expireTime = 100)
    @PostMapping("/lock-key-from-object")
    public R lock2(@RequestBody User user) {
        for (int i = 0; i < 1000000; i++) {
            log.info("waiting......");
        }
        return R.success();
    }
}