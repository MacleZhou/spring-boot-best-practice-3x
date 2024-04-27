package cn.javastack.data.saver.infra.repository.dao.mapper;

import cn.javastack.data.saver.infra.repository.dao.entity.ProductTable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductMapper extends BaseMapper<String, ProductTable>{

    public ProductTable get(String companyCode, String code){
        for (Map.Entry<String, ProductTable> entry : super.getTableData().entrySet()) {
            String k = entry.getKey();
            ProductTable v = entry.getValue();
            if (v.getCode().equals(code) && v.getCompanyCode().equals(companyCode)) {
                return v;
            }
        }
        return null;
    }
}
