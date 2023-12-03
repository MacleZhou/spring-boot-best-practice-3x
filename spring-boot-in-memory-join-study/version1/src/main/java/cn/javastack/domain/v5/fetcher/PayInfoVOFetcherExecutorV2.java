package cn.javastack.domain.v5.fetcher;

import cn.javastack.domain.model.entity.PayInfo;
import cn.javastack.domain.repository.PayInfoRepository;
import cn.javastack.domain.model.vo.PayInfoVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayInfoVOFetcherExecutorV2
        extends BaseItemFetcherExecutor<PayInfoVOFetcherV2, PayInfo, PayInfoVO> {
    @Autowired
    private PayInfoRepository payInfoRepository;

    @Override
    protected Long getFetchId(PayInfoVOFetcherV2 fetcher) {
        return fetcher.getPayInfoId();
    }

    @Override
    protected List<PayInfo> loadData(List<Long> ids) {
        return this.payInfoRepository.getByOrderIds(ids);
    }

    @Override
    protected Long getDataId(PayInfo payInfo) {
        return payInfo.getId();
    }

    @Override
    protected PayInfoVO convertToVo(PayInfo payInfo) {
        return PayInfoVO.apply(payInfo);
    }

    @Override
    protected void setResult(PayInfoVOFetcherV2 fetcher, List<PayInfoVO> payInfoVOs) {
        if (CollectionUtils.isNotEmpty(payInfoVOs)) {
            fetcher.setPayInfo(payInfoVOs.get(0));
        }
    }

    @Override
    public boolean support(Class<PayInfoVOFetcherV2> cls) {
        // 暂时忽略，稍后会细讲
        return PayInfoVOFetcherV2.class.isAssignableFrom(cls);
    }
}