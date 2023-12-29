package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6;

import java.util.HashSet;
import java.util.Set;

public class CategoryCodeFetcherV61 {
    public static <T> Set<String> fetch(AggregatedPOV61 aggregatedPO){
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
