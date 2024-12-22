package cn.javastack.springboot.aop.plugin;

public enum OperateEvent {
    ADD("add"),
    DELETE("delete");

    private String value;

    OperateEvent(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
