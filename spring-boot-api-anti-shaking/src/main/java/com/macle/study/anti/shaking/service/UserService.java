package com.macle.study.anti.shaking.service;


import com.macle.study.anti.shaking.api.dto.AddReq;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResponseEntity<String> add(AddReq addReq) {
        return ResponseEntity.ok("success");
    }
}
