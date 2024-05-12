package com.macle.study.dynamicapi.base;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestHandler<T> {
    private Map<String, DynamicApi> apiMap = new HashMap<>();

    public void registerApi(String apiPath, DynamicApi api) {
        apiMap.put(apiPath, api);
    }

    public ResponseEntity<?> handleRequest(String apiPath, T request) throws Exception {
        DynamicApi api = apiMap.get(apiPath);
        if (api == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API not found");
        }
        return api.handleRequest(request);
    }
}