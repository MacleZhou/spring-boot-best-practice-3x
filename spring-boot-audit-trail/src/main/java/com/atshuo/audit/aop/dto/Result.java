package com.atshuo.audit.aop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code; // 1成功， 0 失败
    private String msg; // 错误消息
    private T data; // 返回数据

    private Result(){

    }
    private Result(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static <T> Result<T> success(T data ){
        return new Result<>(1, "success", data);
    }

    public static <T> Result<T> fail(String msg){
        return new Result<>(0, msg, null);
    }
}
