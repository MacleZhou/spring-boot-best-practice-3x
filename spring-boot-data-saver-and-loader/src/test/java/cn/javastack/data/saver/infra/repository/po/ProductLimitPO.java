package cn.javastack.data.saver.infra.repository.po;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductLimitPO {
    private String id;
    private String productId;
    private String coveredRole;
    private String category;
    private int amount;
}
