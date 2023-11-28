package cn.javastack.core.wide.support;

import cn.javastack.core.wide.Wide;
import cn.javastack.core.wide.WideIndexCompareContext;
import cn.javastack.core.wide.WideIndexSingleUpdateContext;
import cn.javastack.core.wide.WideItemType;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public abstract class BindFromBasedWide<ID, ITEM_TYPE extends Enum<ITEM_TYPE> & WideItemType<ITEM_TYPE>>
        implements Wide<ID, ITEM_TYPE> {
    @Override
    public boolean isSameWithItem(WideIndexCompareContext<ITEM_TYPE> context) {
        return context.getWideWrapper().isSameWithItem(context.getItemData());
    }

    @Override
    public void updateByItem(WideIndexSingleUpdateContext<ITEM_TYPE> context) {
        context.getWideWrapper().updateItem(context.getItemData());
    }
}
