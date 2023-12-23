package cn.javastack.apis;

import cn.javastack.constants.ConstantService;
import cn.javastack.constants.KeyValue;
import com.google.common.base.Splitter;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Example:
 *
 * Kev-Value Pair
 * 1. 返回全部
 *  http://localhost:8080/constant/kvp  返回[{"type":"pendType","keyValues":[{"key":0,"value":"WAITING"},{"key":1,"value":"RETRY"}]},{"type":"jobStatus","keyValues":[{"key":"SUBMITTED","value":"SUBMITTED"}]
 * 2. 返回指定类型的
 *  http://localhost:8080/constant/kvp/gender, 返回[{key=0, value=男}, {key=1, value=女}]
 *
 *  Key-Value Separate Array
 * 1. 返回全部
 *  http://localhost:8080/constant/kvs [{"type":"pendType","keyValues":{"keys":[0,1],"values":["WAITING","RETRY"]}},{"type":"jobStatus","keyValues":{"keys":["SUBMITTED","REJECTED"],"values":["SUBMITTED","REJECTED"]}}]
 * 2. 返回指定类型的
 *  http://localhost:8080/constant/kvp/gender, 返回[{"keys":[0,1],"values":["WAITING","RETRY"]}]
 *
 * 3. 返回指定类型的键对应的值
 *  http://localhost:8080/constant/k2v/gender/0 返回"男"
 *
 *
 *
 * 2. 返回enum定义情形1
 * http://localhost:8080/constant/kvp/department
 * 2. 返回enum定义情形2
 * http://localhost:8080/constant/kvp/gender
 * 2. 返回enum定义情形3
 * http://localhost:8080/constant/kvp/jobStatus
 * 2. 返回enum定义情形4
 * http://localhost:8080/constant/kvp/pendType
 *
 * 通过Spring bean的方法返回
 * http://localhost:8080/constant/kvp/country
 *http://localhost:8080/constant/kvp/country
 *
 * */

@Slf4j
@RestController
@RequestMapping("/constant/")
public class ConstantController {

    @Resource
    private ConstantService constantService;

    @GetMapping("/kvp")
    public List<ConstantKeyValue> finaAllConstantsKvp(){
        List<ConstantKeyValue> valueList = new ArrayList<>();
        constantService.findAll().forEach((key, kvs) -> {
            String type = (String)key;
            valueList.add(new ConstantKeyValue(type, (List<KeyValue>)kvs));
        });
        return valueList;
    }

    @GetMapping("/kvs")
    @ApiOperation("查询系统枚举数据")
    public List<ConstantKeyValues> findAllConstantsKvs() {
        List<ConstantKeyValues> valueList = new ArrayList<>();
        constantService.findAll().forEach((key, value) -> {
            String type = (String)key;
            List<KeyValue> keyValues = (List<KeyValue>)value;
            List keys = new ArrayList(keyValues.size());
            List values = new ArrayList(keyValues.size());
            for (int i = 0; i < keyValues.size(); i++) {
                KeyValue keyValue = keyValues.get(i);
                keys.add(keyValue.getKey());
                values.add(keyValue.getValue());
            }
            valueList.add(new ConstantKeyValues(type, new KeyValues(keys, values)));
        });
        return valueList;
    }

    @GetMapping("/kvp/{type}")
    @ApiOperation("查询指定的系统枚举数据")
    public List<KeyValue> findConstantsKVPByType(@PathVariable String type, HttpServletRequest request) {
        return constantService.get(type, this.parseRequestVariablesToMap(request));
    }

    @GetMapping("/kvs/{type}")
    @ApiOperation("查询指定的系统枚举数据")
    public KeyValues findConstantsKVSByType(@PathVariable String type, HttpServletRequest request) {
        List<KeyValue> keyValues = constantService.get(type, this.parseRequestVariablesToMap(request));
        List keys = new ArrayList(keyValues.size());
        List values = new ArrayList(keyValues.size());
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue keyValue = keyValues.get(i);
            keys.add(keyValue.getKey());
            values.add(keyValue.getValue());
        }
        return new KeyValues(keys, values);
    }

    @GetMapping("/k2v/{type}/{key}")
    @ApiOperation("查询指定的系统枚举数据Key获取Value")
    public Object findConstantValueByTypeKey(@PathVariable String type, @PathVariable String key, HttpServletRequest request) {
        List<KeyValue> keyValues = constantService.get(type, this.parseRequestVariablesToMap(request));
        for (int i = 0; i < keyValues.size(); i++) {
            KeyValue keyValue = keyValues.get(i);
            if(keyValue.getKey().toString().equals(key)){
                return keyValue.getValue();
            }
        }
        return null;
    }




    @Getter
    @AllArgsConstructor
    static class ConstantKeyValue{
        private String type;
        private List<KeyValue> keyValues;
    }

    @Getter
    @AllArgsConstructor
    static class ConstantKeyValues{
        private String type;
        private KeyValues keyValues;
    }

    @Getter
    @AllArgsConstructor
    static class KeyValues{
        private List keys;
        private List values;
    }

    private Map<String, String> parseRequestVariablesToMap(HttpServletRequest request){
        String queryString = request.getQueryString();
        if(StringUtils.isBlank(queryString)){
            return Collections.EMPTY_MAP;
        }
        List<String> list = Splitter.on("&").splitToList(queryString);
        Map<String, String> variables = new HashMap<>();
        list.forEach(str -> {
            if(StringUtils.isNoneBlank(str)){
                str = str.replace("%20", " ");
                str = str.trim();
                str = str.replace("+", " ");
                List<String> nameValuePair = Splitter.on("=").splitToList(str);
                String name = nameValuePair.get(0);
                String value = nameValuePair.size() == 2 ? nameValuePair.get(1) : null;
                if(value != null){
                    variables.put(name, value);
                }
            }
        });
        return variables;
    }
}
