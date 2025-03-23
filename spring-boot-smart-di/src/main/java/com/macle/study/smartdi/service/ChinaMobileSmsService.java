package com.macle.study.smartdi.service;

import com.burukeyou.smartdi.annotations.BeanAliasName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@BeanAliasName("China Mobile SMS Service")
@Slf4j
@Component
public class ChinaMobileSmsService implements SmsService {

    @Override
    public String sendSms(String mobile, String content) {
       return  "Sent by China Mobile Service";
    }
}
