package com.macle.study.smartdi;

import com.burukeyou.smartdi.annotations.EnableSmartDI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSmartDI
@SpringBootApplication
public class SmartDIApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartDIApplication.class, args);
    }
}
