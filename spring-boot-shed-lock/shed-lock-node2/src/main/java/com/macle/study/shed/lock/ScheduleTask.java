package com.macle.study.shed.lock;

/**
 * @Scheduled(cron = "0 0/1 * * * ?")
 * 定义定时任务的调度时间。
 * 表达式含义：任务每分钟的整点触发，例如 12:00、12:01、12:02。
 * 注意：多个实例（分布式环境）都会在同一时间调度到此任务，但通过 ShedLock 确保只有一个实例能真正执行。
 *
 * @SchedulerLock
 * 锁的最短持有时间为 1分钟。
 * 即使任务提前完成，锁仍会持有至少 1 分钟，防止其他实例快速重复执行任务。
 * 锁的最长持有时间为 10分钟。
 * 如果任务运行超出 10 分钟，即使没有主动释放锁，也会自动过期，其他实例可以继续获取锁。
 * 定义锁的唯一标识。共享存储（如数据库或 Redis）中会记录此锁的状态。
 * 使用 ShedLock 来管理分布式锁。
 * name = "scheduledTaskName":
 * lockAtMostFor = "PT10M":
 * lockAtLeastFor = "PT1M":
 *
 * 任务逻辑
 * System.out.println("Executing scheduled task"); 是任务的业务逻辑。
 * 此逻辑只会在获得锁的实例上执行。
 *
 * */
import jakarta.annotation.Resource;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class ScheduleTask {

    @Resource
    private Environment env;

    @Scheduled(cron = "0 0/1 * * * ?")
    @SchedulerLock(name = "scheduledTaskName", lockAtMostFor = "PT10M", lockAtLeastFor = "PT1M")
    public void scheduledTask() {
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        //  some logic code
        System.out.println("Executing scheduled task- port: " + env.getProperty("server.port"));
    }

}