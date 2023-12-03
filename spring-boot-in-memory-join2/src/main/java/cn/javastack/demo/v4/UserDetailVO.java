package cn.javastack.demo.v4;


import cn.javastack.demo.service.user.User;
import cn.javastack.demo.vo.AddressVO;
import cn.javastack.joininmemory.annotation.JoinInMemory;

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
