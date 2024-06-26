package cn.javastack.jimdemo.service.user;

import cn.javastack.jimdemo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Repository
@Slf4j
public class UserRepository {
    public List<User> getByIds(List<Long> ids){
        TimeUtils.sleepAsMS(10);
        return ids.stream()
                .distinct()
                .map(id -> createUser(id))
                .collect(toList());
    }

    public User getById(Long id){
        log.info("Get User By Id {}", id);
        TimeUtils.sleepAsMS(3);
        return createUser(id);
    }

    private User createUser(Long id){
        return User.builder()
                .id(id)
                .name("用户-" + id)
                .build();
    }
}
