package com.macle.study.smartdi.service;

import com.burukeyou.smartdi.proxyspi.spi.EnvironmentProxySPI;

@EnvironmentProxySPI("${sms.impl}")
public interface SmsService {
    String sendSms(String mobile, String content);
}
