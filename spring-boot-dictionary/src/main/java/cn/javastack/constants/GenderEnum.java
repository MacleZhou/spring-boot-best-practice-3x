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
        /*
        return Stream.of(values())
                .map(v -> new KeyValue(v.key(), v.value()))
                .collect(new QriverCollector<KeyValue<Integer, String>>());
        */
        /*
        List<KeyValue<Integer, String>> keyValues = new ArrayList<>();
        GenderEnum[] genderEnums = values();
        for (int i = 0; i < genderEnums.length; i++) {
            GenderEnum genderEnum = genderEnums[i];
            KeyValue<Integer, String> keyValue = new KeyValue<>(genderEnum.value, genderEnum.name);
            keyValues.add(keyValue);
        }
        return keyValues;

         */
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