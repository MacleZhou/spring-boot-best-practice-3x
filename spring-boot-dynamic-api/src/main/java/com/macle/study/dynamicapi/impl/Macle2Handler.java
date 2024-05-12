package com.macle.study.dynamicapi.impl;

import com.macle.study.dynamicapi.base.DynamicApi;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Macle2Handler implements DynamicApi<Macle2Request> {

    @Override
    public ResponseEntity<?> handleRequest(Macle2Request request) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", request.getName());
        jsonObject.put("gender", "MALE");
        return ResponseEntity.ok(jsonObject);
    }
}
