package cn.javastack.joininmemory.demo;

import java.util.List;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface OrderDetailService {
    List<? extends OrderDetailVO> getByUserId(Long userId);
}
