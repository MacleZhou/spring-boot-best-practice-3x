package com.macle.study.easyrule.model;

import lombok.Data;

@Data
public class RuleResult {
    /**
     * 规则主键
     */
    Long ruleId;
    /**
     * 规则名
     * 默认false
     */
    boolean isMatch = false;

    /**
     * 规则名
     */
    String value;

    /**
     * 满足规则后执行
     */
    public void setValue(String msg){
        this.value = msg;
        this.isMatch = true;
    }
}