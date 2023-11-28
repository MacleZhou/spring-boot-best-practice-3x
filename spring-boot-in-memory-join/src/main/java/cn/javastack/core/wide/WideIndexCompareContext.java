package cn.javastack.core.wide;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class WideIndexCompareContext<ITEM_TYPE extends Enum<ITEM_TYPE> & WideItemType<ITEM_TYPE>>
        extends WideIndexContext<ITEM_TYPE>{

    private WideItemData<ITEM_TYPE, ?> itemData;

    private WideWrapper wideWrapper;

    public WideIndexCompareContext(ITEM_TYPE type, WideItemData<ITEM_TYPE, ?> itemData, WideWrapper wideWrapper){
        setType(type);
        setItemData(itemData);
        setWideWrapper(wideWrapper);
    }
}
