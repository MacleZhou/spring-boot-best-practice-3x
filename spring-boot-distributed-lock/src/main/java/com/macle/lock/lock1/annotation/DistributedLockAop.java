package com.macle.lock.lock1.annotation;

import com.macle.lock.lock1.api.res.R;
import com.macle.lock.lock1.util.AopUtil;
import com.macle.lock.lock1.util.SpelUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class DistributedLockAop {

    @PostConstruct
    public void init() {
        log.info("DistributedLockAop init ......");
    }

    //@Autowired(required = false)
    //private Redisson redisson;

    @Resource
    private RedissonClient redissonClient;

    private static final String LOCK_KEY_PREFIX = "LOCK:";

    /**
     * 拦截加了DistributedTask注解的方法请求
     *
     * @param joinPoint
     * @param distributedLock
     * @return
     * @throws Throwable
     */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        Object result = null;

        String lockKey = LOCK_KEY_PREFIX + SpelUtil.generateKeyBySpEL(distributedLock.lockKey(), joinPoint);

        RLock lock = this.redissonClient.getLock(lockKey);

        boolean locked = false;
        try {
            // 尝试获取分布式锁, 并设置超时时间
            locked = lock.tryLock(0, distributedLock.expireTime(), TimeUnit.SECONDS);
            if (!locked) {
                log.info("#Redisson分布式锁# 加锁失败: [lockKey: {}, ThreadName :{}]", lockKey, Thread.currentThread().getName());
                // 判断目标方法是否有返回值
                if (AopUtil.isReturnVoid(joinPoint)) {
                    return null;
                } else {
                    return R.fail(distributedLock.message());
                }
            }
            log.info("#Redisson分布式锁# 加锁成功: [lockKey: {}, ThreadName :{}]", lockKey, Thread.currentThread().getName());

            // 执行目标方法
            result = joinPoint.proceed();
        } finally {
            if (locked) {
                lock.unlock();
                log.info("#Redisson分布式锁# 解锁成功: [lockKey: {}, ThreadName :{}]", lockKey, Thread.currentThread().getName());
            }
        }
        return result;
    }
}