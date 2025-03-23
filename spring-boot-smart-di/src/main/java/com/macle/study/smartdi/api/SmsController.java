package com.macle.study.smartdi.api;

import com.burukeyou.smartdi.proxyspi.spi.AutowiredProxySPI;
import com.burukeyou.smartdi.proxyspi.spi.AutowiredSPI;
import com.macle.study.smartdi.service.SmsService;
import com.macle.study.smartdi.service.SmsService2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
    @AutowiredSPI
    private SmsService smsService;

    @AutowiredSPI
    private SmsService2 smsService2;

    @GetMapping("/sms")
    public String sms(){
        return smsService.sendSms(null, null);
    }

    @GetMapping("/sms2")
    public String sms2(){
        return smsService2.sendSms(null, null);
    }
}
