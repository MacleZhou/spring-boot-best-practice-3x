package cn.javastack.data.saver.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductLimitTable extends BaseTable<String>{
    private String productId;
    private String coveredRole;
    private String category;
    private int amount;
}
