package cn.javastack.core.wide;

/**
 * Created by taoli on 2022/10/28.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideWrapperFactory<W extends Wide>   {
    WideWrapper<W> createForWide(W wide);
}
