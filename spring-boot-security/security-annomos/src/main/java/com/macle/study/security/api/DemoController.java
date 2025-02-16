package com.macle.study.security.api;


import com.macle.study.security.config.AnonymousAccess;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Resource
    private SecurityContext securityContext;

    @GetMapping("anonymous")
    @AnonymousAccess
    public String loadCondition() {
        return "anonymous";
    }

    @GetMapping("security")
    public String security() {
        Authentication authentication = securityContext.getAuthentication();
        return "security" + authentication.getPrincipal();
    }
}
