package com.macle.study.smartdi.service;

import com.macle.study.smartdi.annotation.DBProxySPI;

@DBProxySPI("${sms.impl2}")
public interface SmsService2 {
    String sendSms(String mobile, String content);
}
