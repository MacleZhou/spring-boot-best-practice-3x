package com.macle.study.security.api;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/api-security")
@RestController
public class ApiSecurityController {

    @GetMapping("/user")
    public String user() {
        return "get user";
    }

    @PutMapping("/user")
    public String updateUser() {
        return "update user";
    }
}
