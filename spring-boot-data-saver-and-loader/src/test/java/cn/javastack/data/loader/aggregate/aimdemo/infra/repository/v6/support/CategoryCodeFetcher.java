package cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6.support;

import cn.javastack.data.loader.aggregate.aimdemo.infra.repository.v6.dvo.AggregatedDVO;

import java.util.HashSet;
import java.util.Set;

public class CategoryCodeFetcher {
    public static Set<String> fetch(AggregatedDVO aggregatedDVO){
        Set<String> categoryCodes = new HashSet<>();
        categoryCodes.add("LL");
        categoryCodes.add("AL");
        categoryCodes.add("DL");
        categoryCodes.add("VL");
        return categoryCodes;
    }
}
