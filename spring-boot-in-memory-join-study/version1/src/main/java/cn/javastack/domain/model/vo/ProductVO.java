package cn.javastack.domain.model.vo;

import cn.javastack.domain.model.entity.Product;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductVO {
    private Long id;
    private String name;
    private Integer price;

    private ProductVO(){}

    public static ProductVO apply(Product product){
        return new ProductVO();
    }

}
