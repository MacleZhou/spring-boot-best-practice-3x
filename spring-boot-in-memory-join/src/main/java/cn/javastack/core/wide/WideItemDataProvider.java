package cn.javastack.core.wide;

import cn.javastack.core.SmartComponent;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.List;

/**
 * Created by taoli on 2022/10/29.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Order
public interface WideItemDataProvider<
        TYPE extends Enum<TYPE> & WideItemType<TYPE>,
        KEY,
        ITEM extends WideItemData<TYPE, ?>>
    extends SmartComponent<TYPE> {

    @Override
    default boolean support(TYPE type){
        return getSupportType() == type;
    }

    default ITEM apply(KEY key){
        if (key == null){
            return null;
        }
        List<ITEM> items = apply(Collections.singletonList(key));
        if (CollectionUtils.isNotEmpty(items)){
            return items.get(0);
        }
        return null;
    }

    List<ITEM> apply(List<KEY> key);

    TYPE getSupportType();
}
