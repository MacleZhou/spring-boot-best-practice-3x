package cn.javastack.springboot;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 微信公众号：Java技术栈
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class ShedLock1Application {

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(ShedLock1Application.class);
    }


}
