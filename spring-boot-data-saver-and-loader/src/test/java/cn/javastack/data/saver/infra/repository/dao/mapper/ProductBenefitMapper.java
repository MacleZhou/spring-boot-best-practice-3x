package cn.javastack.data.saver.infra.repository.dao.mapper;

import cn.javastack.data.saver.infra.repository.dao.entity.ProductBenefitTable;
import cn.javastack.data.saver.infra.repository.dao.entity.ProductLimitTable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductBenefitMapper extends BaseMapper<String, ProductBenefitTable> {

    public List<ProductBenefitTable> getByProductId(String productId){
        List<ProductLimitTable> results = null;
        for (Map.Entry<String, ProductBenefitTable> entry : super.getTableData().entrySet()) {
            String id = entry.getKey();
            ProductBenefitTable data = entry.getValue();
            if (data.getProductId().equals(productId)) {
                if (results == null) {
                    results = new ArrayList<>();
                }
                results.add(data);
            }
        }
        return results;
    }
}
