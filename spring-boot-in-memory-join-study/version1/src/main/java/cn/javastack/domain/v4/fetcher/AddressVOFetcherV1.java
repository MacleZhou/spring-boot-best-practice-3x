package cn.javastack.domain.v4.fetcher;

import cn.javastack.domain.model.vo.AddressVO;

public interface AddressVOFetcherV1 {
    Long getAddressId();

    void setAddress(AddressVO address);
}
