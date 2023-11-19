package cn.javastack.constants;

import cn.javastack.infra.utils.Utils;

import java.util.List;

@Constant("gender")
public enum GenderEnum implements IKeyValue<Integer, String> {

    MALE(0, "男"),
    FEMALE(1, "女");

    private Integer value;
    private String name;

    GenderEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public  List<KeyValue<Integer, String>> keyValues() {
        return Utils.converts(values());
    }

    @Override
    public Integer key() {
        return value;
    }

    @Override
    public String value() {
        return name;
    }
}