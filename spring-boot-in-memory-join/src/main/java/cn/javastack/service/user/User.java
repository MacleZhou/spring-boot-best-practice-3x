package cn.javastack.service.user;

import cn.javastack.core.wide.WideItemData;
import cn.javastack.wide.WideOrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UserS")
@Table(name = "t_user_s")
public class User implements WideItemData<WideOrderType, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public boolean isEnable() {
        return true;
    }

    @Override
    public WideOrderType getItemType() {
        return WideOrderType.USER;
    }

    @Override
    public Long getKey() {
        return id;
    }
}
