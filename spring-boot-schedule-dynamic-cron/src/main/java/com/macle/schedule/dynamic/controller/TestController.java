package com.macle.schedule.dynamic.controller;

import com.macle.schedule.dynamic.tasks.ScheduleTask2;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.macle.schedule.dynamic.tasks.ScheduleTask;

/**
 * @author wl
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private ScheduleTask scheduleTask;

    @Resource
    private ScheduleTask2 scheduleTask2;

    @GetMapping("/updateCron")
    public String updateCron(String cron) {
        log.info("new cron :{}", cron);
        scheduleTask.setCron(cron);
        return "ok";
    }

    @GetMapping("/updateTimer")
    public String updateTimer(Long timer) {
        log.info("new timer :{}", timer);
        scheduleTask2.setTimer(timer);
        return "ok";
    }
}