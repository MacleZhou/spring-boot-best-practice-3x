package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.entity.User;
import cn.javastack.domain.model.vo.UserVO;
import cn.javastack.domain.repository.UserRepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserVOFetcherExecutorV3
        extends BaseItemFetcherExecutor<UserVOFetcherV3, User, UserVO> {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected Long getFetchId(UserVOFetcherV3 fetcher) {
        return fetcher.getUserId();
    }

    @Override
    protected List<User> loadData(List<Long> ids) {
        return this.userRepository.getByIds(ids);
    }

    @Override
    protected Long getDataId(User user) {
        return user.getId();
    }

    @Override
    protected UserVO convertToVo(User user) {
        return UserVO.apply(user);
    }

    @Override
    protected void setResult(UserVOFetcherV3 fetcher, List<UserVO> userVO) {
        if (CollectionUtils.isNotEmpty(userVO)) {
            fetcher.setUser(userVO.get(0));
        }
    }

    @Override
    public boolean support(Class<UserVOFetcherV3> cls) {
        // 暂时忽略，稍后会细讲
        return UserVOFetcherV3.class.isAssignableFrom(cls);
    }
}