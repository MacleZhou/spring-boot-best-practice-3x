package cn.javastack.jimdemo.v341.fetcher;


import cn.javastack.jimdemo.vo.AddressVO;

public interface AddressVOFetcherV1 {
    Long getAddressId();

    void setAddress(AddressVO address);
}
