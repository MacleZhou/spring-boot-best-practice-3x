package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.vo.PayInfoVO;

public interface PayInfoVOFetcherV3 extends ItemFetcher<PayInfoVO> {
    Long getPayInfoId();

    void setPayInfo(PayInfoVO payInfo);

    @Override
    default public Long getFetchId(){
        return this.getPayInfoId();
    }

    @Override
    default public void setResult(PayInfoVO payInfoVO){
        this.setPayInfo(payInfoVO);
    }
}
