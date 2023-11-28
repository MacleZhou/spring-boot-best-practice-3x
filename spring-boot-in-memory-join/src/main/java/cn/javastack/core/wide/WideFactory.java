package cn.javastack.core.wide;

/**
 * Created by taoli on 2022/10/27.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideFactory<MASTER_ID, W extends Wide> {
    W create(MASTER_ID  masterId);
}
