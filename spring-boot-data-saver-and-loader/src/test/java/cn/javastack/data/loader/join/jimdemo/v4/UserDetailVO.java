package cn.javastack.data.loader.join.jimdemo.v4;


import cn.javastack.data.loader.annotation.DataHolderConfig;
import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.annotation.DataLoaderInMemory;
import cn.javastack.data.loader.join.jimdemo.service.user.User;
import cn.javastack.data.loader.join.jimdemo.vo.AddressVO;

/**
 * Created by taoli on 2023/3/19.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@DataHolderConfig(dataHolderType = DataHolderType.JOIN)
public class UserDetailVO {
    private User user;

    @DataLoaderInMemory(keyFromSourceData = "#{user.addressId}",
            keyFromJoinData = "#{id}",
            loader = "#{@addressRepository.getByIds(#root)}",
            dataConverter = "#{T(cn.javastack.data.loader.join.jimdemo.vo.AddressVO).apply(#root)}"
    )
    private AddressVO address;
}
