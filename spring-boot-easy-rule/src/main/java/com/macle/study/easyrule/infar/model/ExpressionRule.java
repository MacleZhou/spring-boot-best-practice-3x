package com.macle.study.easyrule.infar.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@TableName("EXPRESSION_RULE")
public class ExpressionRule {
    /**
     * 主键
     */
    Long id;
    /**
     * 规则分组
     */
    String ruleGroup;
    /**
     * 规则标识（英文，且唯一）
     */
    String ruleCode;
    /**
     * 规则名
     */
    String ruleName;

    /**
     * 规则描述
     */
    String ruleDescription;

    /**
     * 满足规则后的消息
     */
    String message;

    /**
     * 规则表达式，通过conditionList生成
     */
    String whenExpression;

    /**
     * 匹配规则操作表达式
     */
    String thenExpression;

    /**
     * 创建人工号
     */
    String creatUser;

    /**
     * 创建时间
     */
    Date createTime;

    /**
     * 最近更新时间
     */
    Date lastUpdateTime;

    /**
     * 最近更新人
     */
    String lastUpdateUser;

    /**
     * 是否启用
     */
    boolean isEnable;

    /**
     * 条件列表
     */
    @TableField(exist = false)
    List<ExpressionRuleCondition> conditionList;
}
