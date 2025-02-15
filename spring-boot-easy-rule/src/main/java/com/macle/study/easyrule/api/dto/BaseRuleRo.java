package com.macle.study.easyrule.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@ToString
public class BaseRuleRo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "id")
    Long id;
    /**
     * 规则组
     */
    @ApiModelProperty(value = "ruleGroup",required = true)
    String ruleGroup;
    /**
     * 规则标识（英文，且唯一）
     */
    @ApiModelProperty(value = "ruleCode",required = true)
    String ruleCode;
    /**
     * 规则名
     */
    @ApiModelProperty(value = "ruleName",required = true)
    String ruleName;

    /**
     * 规则描述
     */
    @ApiModelProperty(value = "ruleDescription",required = true)
    String ruleDescription;

    /**
     * 条件
     */
    @ApiModelProperty(value = "conditionRoList",required = true)
    List<ConditionRo> conditionRoList;

    /**
     * 满足规则后可以带这个值
     */
    @ApiModelProperty(value = "message",required = true)
    String message;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "isEnable")
    boolean isEnable = true;

    /**
     * 条件表达式
     */
    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String whenExpression;

    /**
     * 结果表达式
     */
    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String thenExpression;

    /**
     * 组装when表达式
     */
    public String getWhenExpression(String factAlias) {
        StringBuilder expression = new StringBuilder();
        conditionRoList.forEach(conditionRo -> {
            expression
                    .append(conditionRo.leftBrackets)
                    .append(factAlias)
                    // 从JSON中取出字段值
                    .append(".get(\"")
                    .append(conditionRo.field)
                    .append("\")")
                    .append(conditionRo.getOperatorExpress(factAlias))
                    .append(conditionRo.rightBrackets)
                    .append(conditionRo.getLinkExpress());
        });
        log.info("whenExpression: {}",expression.toString());
        return expression.toString();
    }

    /**
     * 组装then表达式
     * 不做太过灵活的操作，值返回是否命中规则，至于命中后改干什么业务代码自己实现
     */
    public String getThenExpression(String resultAlias){
        StringBuilder expression = new StringBuilder();
        expression.append(resultAlias).append(".setValue(\"").append(message).append("\");");
        log.info("thenExpression: {}",expression.toString());
        return expression.toString();
    }
}