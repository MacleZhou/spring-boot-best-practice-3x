package com.macle.study.dynamicapi.impl;

import com.macle.study.dynamicapi.base.DynamicApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Macle1Handler implements DynamicApi<Macle1Request> {

    @Override
    public ResponseEntity<?> handleRequest(Macle1Request request) {
        return ResponseEntity.ok("Macle1Handler");
    }
}
