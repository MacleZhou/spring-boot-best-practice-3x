package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.vo.AddressVO;

public interface AddressVOFetcherV2 extends ItemFetcher<AddressVO> {
    Long getAddressId();

    void setAddress(AddressVO address);
}
