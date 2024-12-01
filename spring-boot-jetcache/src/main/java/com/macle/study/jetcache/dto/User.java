package com.macle.study.jetcache.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private Long id;
    private String name;
    private Integer age;
    private Integer sex;
}