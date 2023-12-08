package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.vo.UserVO;

public interface UserVOFetcherV2 extends ItemFetcher<UserVO> {
    Long getUserId();

    @Override
    default Long getFetchId() {
        return getUserId();
    }

    void setUser(UserVO user);

    @Override
    default void setResult(UserVO userVO){
        this.setUser(userVO);
    }
}
