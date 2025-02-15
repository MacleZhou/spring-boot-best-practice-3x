package cn.javastack.springboot.schedule.tasks;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShedTask {

    /**
     * 启动Redis
     * 启动ShedLock1Application
     * 启动ShedLock2Application
     * 此任务只会在一个终端执行，其它的不会执行
     * */

    @Scheduled(fixedRateString = "${fixed.rate.in.milliseconds}")
    @SchedulerLock(name = "scheduledTaskName", lockAtLeastFor = "PT10S", lockAtMostFor = "PT30S")
    public void scheduledTask() {
        log.info("Executing scheduled shed lock task");
        // Simulate some work by sleeping
        try {
            Thread.sleep(15000); // Sleep for 15 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
