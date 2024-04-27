package cn.javastack.data.saver.infra.repository.po;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ProductPO {
    private String id;
    private String code;
    private String name;

    private String companyCode;
}
