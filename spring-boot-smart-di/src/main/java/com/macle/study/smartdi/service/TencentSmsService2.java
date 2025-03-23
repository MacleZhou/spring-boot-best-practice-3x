package com.macle.study.smartdi.service;

import com.burukeyou.smartdi.annotations.BeanAliasName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@BeanAliasName("TencentSMSService2")
@Slf4j
@Component("TencentSMSService2")
public class TencentSmsService2 implements SmsService2 {

    @Override
    public String sendSms(String mobile, String content) {
        return "Sent by Tencent Service2";
    }
}
