package cn.javastack.domain.v4.fetcher;

import cn.javastack.domain.model.vo.UserVO;

public interface UserVOFetcherV1 {
    Long getUserId();

    void setUser(UserVO user);
}
