package cn.javastack.core.wide;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by taoli on 2022/10/27.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface Wide<ID, ITEM_TYPE extends Enum<ITEM_TYPE> & WideItemType<ITEM_TYPE>> {
    ID getId();

    boolean isValidate();

    boolean isSameWithItem(WideIndexCompareContext<ITEM_TYPE> context);

    void updateByItem(WideIndexSingleUpdateContext<ITEM_TYPE> context);

    default void updateByItem(WideIndexBatchUpdateContext<ITEM_TYPE> context){
        if (CollectionUtils.isNotEmpty(context.getItemDatas())){
            context.getItemDatas().stream()
                    .filter(Objects::nonNull)
                    .map(item -> new WideIndexSingleUpdateContext<>(context.getType(), item, context.getWideWrapper()))
                    .forEach(singleUpdateContext -> updateByItem(singleUpdateContext));
        }
    }

    List<WideItemKey> getItemsKeyByType(ITEM_TYPE type);
}
