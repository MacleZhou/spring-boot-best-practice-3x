package cn.javastack.data.saver.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductLimit{
    private String id;
    private String productId;
    private String coveredRole;
    private String category;
    private int amount;
}
