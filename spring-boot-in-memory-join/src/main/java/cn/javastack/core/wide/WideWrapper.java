package cn.javastack.core.wide;

/**
 * Created by taoli on 2022/10/26.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideWrapper<W extends Wide> {
    W getTarget();

    default <I> void bindItem(I item){
        updateItem(item);
    }

    <I> boolean isSameWithItem(I item);

    <I> void updateItem(I item);
}
