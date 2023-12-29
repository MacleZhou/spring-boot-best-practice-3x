package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.support;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.po.AggregatedPO;

import java.util.HashSet;
import java.util.Set;

public class CategoryCodeFetcher<T extends AggregatedPO> {
    public static <T> Set<String> fetch(AggregatedPO aggregatedPO){
        Set<String> categoryCodes = new HashSet<>();
        aggregatedPO.getBenefitLimits().forEach((s, productBenefitLimitDVOS) -> {
            productBenefitLimitDVOS.forEach(productBenefitLimitDVO -> {
                categoryCodes.add(productBenefitLimitDVO.getCategory());
            });
        });

        aggregatedPO.getProductLimits().forEach(medicalProductLimit -> {
            categoryCodes.add(medicalProductLimit.getCategory());
        });

        aggregatedPO.getProductDeducts().forEach(medicalProductLimit -> {
            categoryCodes.add(medicalProductLimit.getCategory());
        });

        aggregatedPO.getProductCoshares().forEach(medicalProductLimit -> {
            categoryCodes.add(medicalProductLimit.getCategory());
        });
        return categoryCodes;
    }
}
