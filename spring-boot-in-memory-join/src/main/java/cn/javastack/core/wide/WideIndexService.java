package cn.javastack.core.wide;

import java.util.List;

/**
 * Created by taoli on 2022/10/27.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideIndexService<
        MASTER_DATA_ID, // 主数据 ID
        ITEM_TYPE extends Enum<ITEM_TYPE> & WideItemType<ITEM_TYPE> // 关联数据类型
        > {

    void index(MASTER_DATA_ID id);

    void index(List<MASTER_DATA_ID> ids);

    <KEY> void updateItem(ITEM_TYPE type, KEY key);
}
