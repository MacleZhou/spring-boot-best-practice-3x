package cn.javastack.joininmemory.demo.v4;


import cn.javastack.annotation.joininmemory.JoinInMemory;
import cn.javastack.joininmemory.demo.AddressVO;
import cn.javastack.service.user.User;

/**
 * Created by taoli on 2023/3/19.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public class UserDetailVO {
    private User user;

    @JoinInMemory(keyFromSourceData = "#{user.addressId}",
            keyFromJoinData = "#{id}",
            loader = "#{@addressRepository.getByIds(#root)}",
            joinDataConverter = "#{T(cn.javastack.joininmemory.demo.AddressVO).apply(#root)}"
    )
    private AddressVO address;
}
