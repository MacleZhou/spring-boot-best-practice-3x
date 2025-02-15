package com.macle.study.easyrule.api.dto;

public class ResponseRusult<T> {
    private Object T;

    ResponseRusult<T> buildSucc(T t){
        ResponseRusult<T> responseRusult = new ResponseRusult<>();
        responseRusult.T = t;
        return responseRusult;
    }

    ResponseRusult<String> buildFail(String message){
        ResponseRusult<String> responseRusult = new ResponseRusult<>();
        responseRusult.T = message;
        return responseRusult;
    }
}
