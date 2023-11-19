package cn.javastack.constants;

@Constant("gender")
public enum GenderEnum {

    MALE(0, "男"),
    FEMALE(1, "女");

    private Integer value;
    private String name;

    GenderEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
}