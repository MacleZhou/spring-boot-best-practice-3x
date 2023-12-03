package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.vo.UserVO;

public interface UserVOFetcherV2 extends ItemFetcher<UserVO> {
    Long getUserId();

    void setUser(UserVO user);
}
