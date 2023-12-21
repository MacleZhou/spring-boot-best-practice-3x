package cn.javastack.jimdemo.v341.fetcher;

import cn.javastack.jimdemo.vo.UserVO;

public interface UserVOFetcherV1 {
    Long getUserId();

    void setUser(UserVO user);
}
