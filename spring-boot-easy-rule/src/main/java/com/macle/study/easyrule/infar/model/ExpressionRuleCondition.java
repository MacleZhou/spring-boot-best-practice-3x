package com.macle.study.easyrule.infar.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@TableName("EXPRESSION_RULE_CONDITION")
public class ExpressionRuleCondition {

    /**
     * 主键
     */
    Long id;

    /**
     * 主表ID
     */
    Long masterId;

    /**
     * 左括号
     * ( ~ (((((
     */
    String leftBrackets;

    /**
     * 字段
     */
    String field;

    /**
     * 操作
     * 大于,小于,等于,不等于,包含,不包含，前缀，后缀
     * >  , <  , ==, !=  , contains , !contains, startWith, endWith
     *
     */
    String operator;

    /**
     * 值字段类型
     * 自定义输入 或 实体字段
     *  input , field
     */
    String valueType;

    /**
     * 字段规则命中值
     */
    String value;

    /**
     * 右括号
     * ) ~ )))))
     */
    String rightBrackets;

    /**
     * 多条件连接符号
     * && ~ ||
     */
    String link;
}

