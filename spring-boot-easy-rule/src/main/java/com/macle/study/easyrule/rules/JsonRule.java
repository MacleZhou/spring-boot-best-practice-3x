package com.macle.study.easyrule.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;

import java.io.FileReader;

public class JsonRule {

    public static void main(String[] args) throws Exception {
        // 定义规则引擎参数，设置跳过优先级较低的规则
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine rulesEngine = new DefaultRulesEngine(parameters);

        // 从JSON文件中加载规则
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        Rules rules = ruleFactory.createRules(new FileReader("/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-easy-rule/src/main/resources/rules.json"));

        // 定义一个Fact，包含要检查的数字
        Facts facts = new Facts();
        facts.put("number", 6); // 你可以更改这个数字来测试不同的情况

        // 执行规则
        rulesEngine.fire(rules, facts);
    }

}
