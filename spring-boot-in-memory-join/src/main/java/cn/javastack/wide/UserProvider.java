package cn.javastack.wide;

import cn.javastack.core.wide.WideItemDataProvider;
import cn.javastack.service.user.User;
import cn.javastack.wide.jpa.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Component
public class UserProvider implements WideItemDataProvider<WideOrderType, Long, User> {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> apply(List<Long> key) {
        return userDao.findAllById(key);
    }

    @Override
    public WideOrderType getSupportType() {
        return WideOrderType.USER;
    }
}
