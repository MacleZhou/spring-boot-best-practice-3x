package cn.javastack.demo.v341.fetcher;


import cn.javastack.demo.vo.AddressVO;

public interface AddressVOFetcherV1 {
    Long getAddressId();

    void setAddress(AddressVO address);
}
