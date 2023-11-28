package cn.javastack.core.wide;

/**
 * Created by taoli on 2022/10/29.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideItemData<
        T extends Enum<T> & WideItemType<T>,
        KEY> {
    T getItemType();

    KEY getKey();
}
