package cn.javastack.data.loader.aggregate.aimdemo.domain.model.entity;

import java.util.List;

public class Product {
    private String code;
    private String name;

    private List<Limit> limits;

    private List<Deduct> deducts;

    private List<Coshare> coshares;

    private List<Benefit> benefits;
}
