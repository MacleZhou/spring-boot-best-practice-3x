package cn.javastack.demoProductDetail.domain.model.entity;

import lombok.Data;

@Data
public class Category {
    private String code;
    private String shortName;
    private int valueType;
}
