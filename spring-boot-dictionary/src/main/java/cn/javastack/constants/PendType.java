package cn.javastack.constants;

import cn.javastack.infra.utils.Utils;

import java.util.List;

@Constant("pendType")
public enum PendType{
    WAITING(0), RETRY(1);

    int code;

    PendType(int code){
        this.code = code;
    }
}
