package cn.javastack.wide;

import cn.javastack.core.wide.WideItemDataProvider;
import cn.javastack.service.product.Product;
import cn.javastack.wide.jpa.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Component
public class ProductProvider implements WideItemDataProvider<WideOrderType, Long, Product> {
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> apply(List<Long> key) {
        return productDao.findAllById(key);
    }

    @Override
    public WideOrderType getSupportType() {
        return WideOrderType.PRODUCT;
    }
}
