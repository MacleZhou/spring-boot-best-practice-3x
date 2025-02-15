package com.macle.study.csv.domain.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String address;
    private String email;
}
