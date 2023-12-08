package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.entity.PayInfo;
import cn.javastack.domain.model.vo.PayInfoVO;
import cn.javastack.domain.repository.PayInfoRepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayInfoVOFetcherExecutorV3
        extends BaseItemFetcherExecutor<PayInfoVOFetcherV3, PayInfo, PayInfoVO> {
    @Autowired
    private PayInfoRepository payInfoRepository;

    @Override
    protected Long getFetchId(PayInfoVOFetcherV3 fetcher) {
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
    protected void setResult(PayInfoVOFetcherV3 fetcher, List<PayInfoVO> payInfoVOs) {
        if (CollectionUtils.isNotEmpty(payInfoVOs)) {
            fetcher.setPayInfo(payInfoVOs.get(0));
        }
    }

    @Override
    public boolean support(Class<PayInfoVOFetcherV3> cls) {
        // 暂时忽略，稍后会细讲
        return PayInfoVOFetcherV3.class.isAssignableFrom(cls);
    }
}