package com.macle.bean.wrapper.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter @Getter @ToString
public class Employee {
    private String name;
    private float salary;
    private String[] address = new String[2] ;
    private Map<String, String> account = new HashMap<>() ;
}
