package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.vo.AddressVO;

public interface AddressVOFetcherV3 extends ItemFetcher<AddressVO> {
    Long getAddressId();

    void setAddress(AddressVO address);

    @Override
    default public Long getFetchId(){
        return this.getAddressId();
    }

    @Override
    default public void setResult(AddressVO addressVO){
        this.setAddress(addressVO);
    }
}
