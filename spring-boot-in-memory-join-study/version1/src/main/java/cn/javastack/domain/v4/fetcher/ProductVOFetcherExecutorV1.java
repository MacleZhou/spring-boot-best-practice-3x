package cn.javastack.domain.v4.fetcher;

import cn.javastack.domain.model.entity.Product;
import cn.javastack.domain.repository.ProductRepository;
import cn.javastack.domain.model.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
public class ProductVOFetcherExecutorV1 {
    @Autowired
    private ProductRepository productRepository;

    public void fetch(List<? extends ProductVOFetcherV1> fetchers){
        List<Long> ids = fetchers.stream()
                .map(ProductVOFetcherV1::getProductId)
                .distinct()
                .collect(Collectors.toList());

        List<Product> products = productRepository.getByIds(ids);

        Map<Long, Product> productMap = products.stream()
                .collect(toMap(product -> product.getId(), Function.identity()));

        fetchers.forEach(fetcher -> {
            Long productId = fetcher.getProductId();
            Product product = productMap.get(productId);
            if (product != null){
                ProductVO productVO = ProductVO.apply(product);
                fetcher.setProduct(productVO);
            }
        });
    }
}