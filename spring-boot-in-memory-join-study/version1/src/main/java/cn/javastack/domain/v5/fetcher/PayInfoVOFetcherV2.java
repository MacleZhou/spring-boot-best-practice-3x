package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.vo.PayInfoVO;

public interface PayInfoVOFetcherV2 extends ItemFetcher<PayInfoVO> {
    Long getPayInfoId();

    void setPayInfo(PayInfoVO payInfo);
}
