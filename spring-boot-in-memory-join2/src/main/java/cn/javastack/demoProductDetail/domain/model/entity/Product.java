package cn.javastack.demoProductDetail.domain.model.entity;

import java.util.List;

public class Product {
    private String code;
    private String name;

    private List<Category> categories;

    private List<Limit> limits;

    private List<Deduct> deducts;

    private List<Coshare> coshares;

    private List<Benefit> benefits;
}
