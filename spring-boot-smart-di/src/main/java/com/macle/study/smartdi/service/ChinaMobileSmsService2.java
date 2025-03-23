package com.macle.study.smartdi.service;

import com.burukeyou.smartdi.annotations.BeanAliasName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@BeanAliasName("China Mobile SMS Service2")
@Slf4j
@Component("China Mobile SMS Service2")
public class ChinaMobileSmsService2 implements SmsService2 {

    @Override
    public String sendSms(String mobile, String content) {
       return  "Sent by China Mobile Service2";
    }
}
