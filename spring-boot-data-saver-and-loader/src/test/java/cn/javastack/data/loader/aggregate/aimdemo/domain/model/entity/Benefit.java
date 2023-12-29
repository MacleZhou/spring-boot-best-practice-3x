package cn.javastack.data.loader.aggregate.aimdemo.domain.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class Benefit {

    private String code;

    private String name;

    private List<BenefitLimit> benefitLimits;
}
