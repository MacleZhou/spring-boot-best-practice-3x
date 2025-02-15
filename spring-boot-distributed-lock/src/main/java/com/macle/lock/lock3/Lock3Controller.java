package com.macle.lock.lock3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Book;

@Slf4j
@RestController
@RequestMapping("lock3")
public class Lock3Controller {
    @GetMapping("/testRedisLock/{userId}")
    //@RedissionLockAnnotation(lockKey="exampleLockKey", leaseTime = 10000)
    public Book testRedisLock(@PathVariable("userId") Long userId) {
        try {
            log.info("睡眠执行前");
            Thread.sleep(10000);
            log.info("睡眠执行后");
        } catch (Exception e) {
            // log error
            log.info("has some error", e);
        }
        return null;
    }
}
