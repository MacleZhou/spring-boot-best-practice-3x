package cn.javastack.domain.v4.fetcher;

import cn.javastack.domain.model.entity.PayInfo;
import cn.javastack.domain.repository.PayInfoRepository;
import cn.javastack.domain.model.vo.PayInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
public class PayInfoVOFetcherExecutorV1 {
    @Autowired
    private PayInfoRepository payInfoRepository;

    public void fetch(List<? extends PayInfoVOFetcherV1> fetchers){
        List<Long> ids = fetchers.stream()
                .map(PayInfoVOFetcherV1::getPayInfoId)
                .distinct()
                .collect(Collectors.toList());

        List<PayInfo> payInfos = payInfoRepository.getByOrderIds(ids);

        Map<Long, PayInfo> payInfoMap = payInfos.stream()
                .collect(toMap(payInfo -> payInfo.getId(), Function.identity()));

        fetchers.forEach(fetcher -> {
            Long payInfoId = fetcher.getPayInfoId();
            PayInfo payInfo = payInfoMap.get(payInfoId);
            if (payInfo != null){
                PayInfoVO payInfoVO = PayInfoVO.apply(payInfo);
                fetcher.setPayInfo(payInfoVO);
            }
        });
    }
}