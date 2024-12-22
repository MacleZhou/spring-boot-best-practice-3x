package com.macle.study.anti.shaking.constant;

public class BizException extends RuntimeException {
    private ResponseCodeEnum code;
    private String msg;

    public BizException(ResponseCodeEnum code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
