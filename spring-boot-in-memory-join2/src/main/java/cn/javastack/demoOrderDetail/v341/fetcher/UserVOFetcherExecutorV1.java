package cn.javastack.demoOrderDetail.v341.fetcher;

import cn.javastack.demoOrderDetail.service.user.User;
import cn.javastack.demoOrderDetail.service.user.UserRepository;
import cn.javastack.demoOrderDetail.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
public class UserVOFetcherExecutorV1 {
    @Autowired
    private UserRepository userRepository;

    public void fetch(List<? extends UserVOFetcherV1> fetchers){
        List<Long> ids = fetchers.stream()
                .map(UserVOFetcherV1::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<User> users = userRepository.getByIds(ids);

        Map<Long, User> userMap = users.stream()
                .collect(toMap(user -> user.getId(), Function.identity()));

        fetchers.forEach(fetcher -> {
            Long userId = fetcher.getUserId();
            User user = userMap.get(userId);
            if (user != null){
                UserVO userVO = UserVO.apply(user);
                fetcher.setUser(userVO);
            }
        });
    }
}