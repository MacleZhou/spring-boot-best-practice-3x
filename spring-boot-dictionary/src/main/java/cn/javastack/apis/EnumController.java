package cn.javastack.apis;

import cn.javastack.application.ConstantService;
import cn.javastack.constants.KeyValue;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class EnumController {

    @Resource
    private ConstantService constantService;

    @GetMapping("/constants/{type}")
    @ApiOperation("查询指定的系统枚举数据")
    public List<KeyValue> findConstantsByType(@PathVariable String type) {
        return constantService.get(type);
    }

    @GetMapping("/constants/{type}/{key}")
    @ApiOperation("查询指定的系统枚举数据")
    public Object findConstantValueByTypeKey(@PathVariable String type, @PathVariable String key) {
        List<KeyValue> keyValues = constantService.get(type);
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue keyValue = keyValues.get(i);
            if(keyValue.getKey().toString().equals(key)){
                return keyValue.getValue();
            }
        }
        return null;
    }

    @GetMapping("/constants")
    @ApiOperation("查询系统枚举数据")
    public List<ConstantKeyValue> findAllConstants() {
        List<ConstantKeyValue> valueList = new ArrayList<>();
        constantService.findAll().forEach((key, value) -> {
            String type = (String)key;
            List<KeyValue> keyValues = (List<KeyValue>)value;
            valueList.add(new ConstantKeyValue(type, keyValues));
        });
        return valueList;
    }

    @Getter
    @AllArgsConstructor
    static class ConstantKeyValue{
        private String type;
        private List<KeyValue> keyValues;
    }
}
