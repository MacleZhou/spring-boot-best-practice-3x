package cn.javastack.domain.repository;

import cn.javastack.domain.model.entity.PayInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.javastack.infra.utils.TimeUtils.sleepAsMS;
import static java.util.stream.Collectors.toList;

@Repository
@Slf4j
public class PayInfoRepository {
    public PayInfo getByOrderId(Long orderId){
        sleepAsMS(3);
        log.info("Get PayInfo By Id {}", orderId);
        return createProduct(orderId);
    }

    public List<PayInfo> getByOrderIds(List<Long> orderIds){
        sleepAsMS(3);
        return orderIds.stream()
                .map(id -> createProduct(id))
                .collect(toList());
    }

    private PayInfo createProduct(Long id) {
        return PayInfo.builder()
                .id(id)
                .amount(id.intValue() / 100)
                .build();
    }
}
