package cn.javastack.demo;

import cn.javastack.constants.ConstantKV;

@ConstantKV("department")
public enum DepartmentEnum {

    SOFTWARE(1, "SD", "Software Department"),
    MANAGEMENT(2, "MD", "MANAGEMENT Department");

    private int id;

    private String code;

    private String name;

    DepartmentEnum(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}