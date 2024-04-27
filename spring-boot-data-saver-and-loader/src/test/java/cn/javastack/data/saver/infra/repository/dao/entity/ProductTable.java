package cn.javastack.data.saver.infra.repository.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductTable extends BaseTable<String> {
    private String code;
    private String name;

    private String companyCode;
}
