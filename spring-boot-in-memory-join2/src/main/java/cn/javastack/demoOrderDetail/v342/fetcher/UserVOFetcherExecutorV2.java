package cn.javastack.demoOrderDetail.v342.fetcher;

import cn.javastack.demoOrderDetail.service.user.User;
import cn.javastack.demoOrderDetail.service.user.UserRepository;
import cn.javastack.demoOrderDetail.v342.core.BaseItemFetcherExecutor;
import cn.javastack.demoOrderDetail.vo.UserVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserVOFetcherExecutorV2 extends BaseItemFetcherExecutor<OrderDetailVOFetcherV2, User, UserVO> {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected Long getFetchId(OrderDetailVOFetcherV2 fetcher){
        return fetcher.getOrder().getUserId();
    }

    @Override
    protected List<User> loadData(List<Long> ids){
        return this.userRepository.getByIds(ids);
    }

    @Override
    protected Long getDataId(User user){
        return user.getId();
    }

    @Override
    protected UserVO convertToVo(User user){
        return UserVO.apply(user);
    }

    @Override
    protected void setResult(OrderDetailVOFetcherV2 fetcher, List<UserVO> userVOS){
        if (CollectionUtils.isNotEmpty(userVOS)) {
            fetcher.setUser(userVOS.get(0));
        }
    }

    @Override
    public boolean support(Class<OrderDetailVOFetcherV2> cls) {
        return OrderDetailVOFetcherV2.class.isAssignableFrom(cls);
    }
}