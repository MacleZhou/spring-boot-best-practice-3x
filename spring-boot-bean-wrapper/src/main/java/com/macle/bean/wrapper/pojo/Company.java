package com.macle.bean.wrapper.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class Company {
    private String name;
    private Employee employee;
}
