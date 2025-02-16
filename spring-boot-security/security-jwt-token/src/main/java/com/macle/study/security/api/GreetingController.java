package com.macle.study.security.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name="测试接口")
public class GreetingController {
    @GetMapping(value = "/hello")
    @Operation(summary="hello")
    public ResponseEntity<String> hello() {
        String message = "Hello World!";
        return ResponseEntity.ok(message);
    }
}
