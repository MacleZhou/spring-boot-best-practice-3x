package com.macle.study.easyrule.api.dto;

import com.macle.study.easyrule.model.RuleOperation;
import com.macle.study.easyrule.model.UnSupportException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString(callSuper = true)
public class ConditionRo {

    /**
     * 左括号
     * ( ~ (((((
     */
    @ApiModelProperty(value = "leftBrackets",required = true)
    String leftBrackets;

    /**
     * 字段
     */
    @ApiModelProperty(value = "field",required = true)
    String field;

    /**
     * 操作
     * 大于,小于,等于,不等于,包含,不包含，前缀，后缀
     * >  , <  , ==, !=  , contains , !contains, startWith, endWith
     *
     */
    @ApiModelProperty(value = "operator",required = true)
    String operator;

    /**
     * 值字段类型
     * 自定义输入 或 实体字段
     *  input , field
     */
    @ApiModelProperty(value = "valueType",required = true)
    String valueType;

    /**
     * 字段规则命中值
     */
    @ApiModelProperty(value = "value",required = true)
    String value;

    /**
     * 右括号
     * ) ~ )))))
     */
    @ApiModelProperty(value = "rightBrackets",required = true)
    String rightBrackets;

    /**
     * 多条件连接符号
     * && ~ ||
     */
    @ApiModelProperty(value = "link",required = true)
    String link;

    /**
     * 运算符表达式
     */
    String getOperatorExpress(String factAlias) {
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_GREATER_THEN))){
            return ">"+getValueExpress(factAlias);
        }
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_LESS_THEN))){
            return "<"+getValueExpress(factAlias);
        }
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_EQUAL))){
            return "=="+getValueExpress(factAlias);
        }
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_UNQEUAL))){
            return "!="+getValueExpress(factAlias);
        }
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_CONTAIN))){
            return ".contains("+getValueExpress(factAlias)+")";
        }
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_PREFIX))){
            return ".startsWith("+getValueExpress(factAlias)+")";
        }
        if(operator.equalsIgnoreCase(String.valueOf(RuleOperation.OPERATOR_SUFFIX))){
            return ".endsWith("+getValueExpress(factAlias)+")";
        }
        throw new UnSupportException("not support operator:"+operator);
    }

    /**
     * 比较值表达式
     */
    private String getValueExpress(String factAlias) throws UnSupportException {
        if(!valueType.equalsIgnoreCase(String.valueOf(RuleOperation.INPUT_VALUE)) && !valueType.equalsIgnoreCase(String.valueOf(RuleOperation.FIELD_VALUE))){
            throw new UnSupportException(valueType+" is not support ");
        }
        return valueType.equalsIgnoreCase(String.valueOf(RuleOperation.INPUT_VALUE))? String.format("\"%s\"", value) : String.format("%s.get(\"%s\")", factAlias, value);
    }

    /**
     * 连接符表达式
     */
    String getLinkExpress() throws UnSupportException{
        if(StringUtils.isEmpty(link)){
            return "";
        }
        if(link.equalsIgnoreCase(String.valueOf(RuleOperation.LINK_AND))){
            return "&&";
        }
        if(link.equalsIgnoreCase(String.valueOf(RuleOperation.LINK_OR))){
            return "||";
        }
        throw new UnSupportException(link+" is not support ");
    }
}
