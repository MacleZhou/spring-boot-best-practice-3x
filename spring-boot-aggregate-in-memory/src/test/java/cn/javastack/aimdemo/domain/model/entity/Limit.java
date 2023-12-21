package cn.javastack.aimdemo.domain.model.entity;

import lombok.Data;

@Data
public class Limit {
    private String role;
    private String category;
    private String categoryShortName;
    private int ageFrom;
    private int ageTo;
    private String ageUnit;
    private int valueType;
    private int amount;
}
