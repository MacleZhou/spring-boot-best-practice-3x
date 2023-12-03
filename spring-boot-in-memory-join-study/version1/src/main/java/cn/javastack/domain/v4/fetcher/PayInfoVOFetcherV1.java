package cn.javastack.domain.v4.fetcher;

import cn.javastack.domain.model.vo.PayInfoVO;

public interface PayInfoVOFetcherV1 {
    Long getPayInfoId();

    void setPayInfo(PayInfoVO payInfo);
}
