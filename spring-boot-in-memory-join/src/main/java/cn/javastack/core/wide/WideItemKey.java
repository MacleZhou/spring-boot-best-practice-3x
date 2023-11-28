package cn.javastack.core.wide;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WideItemKey<TYPE extends Enum<TYPE> & WideItemType<TYPE>, KEY> {
    private TYPE type;
    private KEY key;
}
