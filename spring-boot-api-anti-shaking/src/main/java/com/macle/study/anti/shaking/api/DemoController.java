package com.macle.study.anti.shaking.api;

import com.macle.study.anti.shaking.annotation.RequestLock;
import com.macle.study.anti.shaking.api.dto.AddReq;
import com.macle.study.anti.shaking.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
    @RequestLock(prefix = "demo_add", expire = 1000)
    public ResponseEntity<String> add(@RequestBody AddReq addReq) {
        return userService.add(addReq);
    }
}
