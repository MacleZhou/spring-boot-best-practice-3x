package com.macle.study.easyrule.rules;

import com.macle.study.easyrule.model.RuleResult;
import com.macle.study.easyrule.infar.model.ExpressionRule;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Slf4j
public class RuleUtils {

    public static RuleResult match(JSONObject dataSource, ExpressionRule rule){
        // 结果
        RuleResult result = new RuleResult();
        // 规则实例
        Facts facts = new Facts();
        facts.put("fact",dataSource);
        facts.put("result",result);
        // 规则内容
        Rule mvelrule = new MVELRule()
                .name(rule.getRuleName())
                .description(rule.getRuleDescription())
                .when(rule.getWhenExpression())
                .then(rule.getThenExpression());
        // 规则集合
        Rules rules = new Rules();
        // 将规则添加到集合
        rules.register(mvelrule);
        //创建规则执行引擎，并执行规则
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
        result.setRuleId(rule.getId());
        return result;
    }

}
