package cn.javastack.demo.v341.fetcher;

import cn.javastack.demo.vo.UserVO;

public interface UserVOFetcherV1 {
    Long getUserId();

    void setUser(UserVO user);
}
