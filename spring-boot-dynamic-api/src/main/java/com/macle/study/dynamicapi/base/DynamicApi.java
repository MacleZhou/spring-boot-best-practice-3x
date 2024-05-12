package com.macle.study.dynamicapi.base;


import org.springframework.http.ResponseEntity;

public interface DynamicApi<T> {
    ResponseEntity<?> handleRequest(T request) throws Exception;
}