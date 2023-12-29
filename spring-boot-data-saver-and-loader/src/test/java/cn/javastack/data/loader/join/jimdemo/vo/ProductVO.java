package cn.javastack.data.loader.join.jimdemo.vo;


import cn.javastack.data.loader.join.jimdemo.service.product.Product;
import lombok.Value;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Value
public class ProductVO {
    private Long id;
    private String name;
    private Integer price;

    public static ProductVO apply(Product product){
        if (product == null){
            return null;
        }
        return new ProductVO(product.getId(), product.getName(), product.getPrice());
    }
}
