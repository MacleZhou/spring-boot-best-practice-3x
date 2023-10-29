package cn.javastack.apis;

import cn.javastack.constants.Constant;
import cn.javastack.constants.IKeyValue;
import cn.javastack.constants.KeyValue;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RestController
public class EnumController {
    private static ConcurrentMap<String, List<KeyValue>> CNT_CACHE = new ConcurrentHashMap<>();

    @GetMapping("/constants/{type}")
    @ApiOperation("查询指定的系统枚举数据")
    public List<KeyValue> findConstantsByType(@PathVariable String type) {
        return CNT_CACHE.get(type);
    }

    @GetMapping("/constants")
    @ApiOperation("查询系统枚举数据")
    public List<ConstantKeyValue> findAllConstants() {
        List<ConstantKeyValue> valueList = new ArrayList<>();
        CNT_CACHE.forEach((type, keyValues) -> {
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

    static {
        Set<String> keys = CNT_CACHE.keySet();
        if (keys.isEmpty()) {
            synchronized (CNT_CACHE) {
                if (keys.isEmpty()) {
                    //Package pkg = GenderEnum.class.getPackage();
                    Reflections reflections = new Reflections("cn.*");
                    reflections.getTypesAnnotatedWith(Constant.class)
                            .forEach(clz -> {
                                try {
                                    if (clz.isEnum()) {
                                        Object[] objects = clz.getEnumConstants();
                                        Constant cEnum = clz.getAnnotation(Constant.class);
                                        IKeyValue constant = ((IKeyValue) objects[0]);
                                        CNT_CACHE.put(cEnum.value(), constant.keyValues());
                                    }
                                } catch (Exception e) {
                                    log.error("", e);
                                }
                            });
                }
            }
        }
    }
}
