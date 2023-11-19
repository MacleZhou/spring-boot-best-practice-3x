package cn.javastack.constants;

@Constant("pendType")
public enum PendType{
    WAITING(0), RETRY(1);

    int code;

    PendType(int code){
        this.code = code;
    }
}
