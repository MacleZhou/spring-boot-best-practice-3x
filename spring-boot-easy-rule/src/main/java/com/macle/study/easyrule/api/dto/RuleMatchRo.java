package com.macle.study.easyrule.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;

@Data
public class RuleMatchRo {
    /**
     * 需要匹配的规则ID列表
     */
    @ApiModelProperty(value = "ruleIds")
    List<Long> ruleIds;

    /**
     * 数据源
     */
    @ApiModelProperty(value = "fact")
    JSONObject fact;
}
