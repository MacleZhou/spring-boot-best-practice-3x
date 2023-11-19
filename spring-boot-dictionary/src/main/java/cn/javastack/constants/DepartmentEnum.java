package cn.javastack.constants;

import cn.javastack.infra.utils.Utils;

import java.util.List;

@Constant("department")
public enum DepartmentEnum implements IKeyValue<String, String> {

    SOFTWARE("S", "Software Department"),
    MANAGEMENT("M", "MANAGEMENT Department");

    private String code;
    private String name;

    DepartmentEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }



    @Override
    public List<KeyValue<String, String>> keyValues() {
        return Utils.converts(values());
    }

    @Override
    public String key() {
        return code;
    }

    @Override
    public String value() {
        return name;
    }
}