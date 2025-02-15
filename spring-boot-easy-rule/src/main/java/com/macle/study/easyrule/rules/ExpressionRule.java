package com.macle.study.easyrule.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

public class ExpressionRule {
    public static void main(String[] args) {
        // 定义规则引擎参数，设置跳过优先级较低的规则
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine rulesEngine = new DefaultRulesEngine(parameters);

        // 定义规则C：优先级最高，当数字能被2和3整除时触发
        MVELRule ruleC = new MVELRule()
                .name("Rule C")
                .priority(1) // 设置优先级为1
                .when("number % 2 == 0 && number % 3 == 0") // 检查是否能被2和3整除
                .then("System.out.println(\"我既可被2整除，也能被3整除\");"); // 触发时的动作

        // 定义规则A：优先级第二，当数字能被2整除时触发
        MVELRule ruleA = new MVELRule()
                .name("Rule A")
                .priority(2) // 设置优先级为2
                .when("number % 2 == 0") // 检查是否能被2整除
                .then("System.out.println(\"我能被2整除\");"); // 触发时的动作

        // 定义规则B：优先级最低，当数字能被3整除时触发
        MVELRule ruleB = new MVELRule()
                .name("Rule B")
                .priority(3) // 设置优先级为3
                .when("number % 3 == 0") // 检查是否能被3整除
                .then("System.out.println(\"我能被3整除\");"); // 触发时的动作

        // 创建规则集并添加规则
        Rules rules = new Rules();
        rules.register(ruleC);
        rules.register(ruleA);
        rules.register(ruleB);

        // 定义一个Fact，包含要检查的数字
        Facts facts = new Facts();
        facts.put("number", 6); // 你可以更改这个数字来测试不同的情况

        // 执行规则
        rulesEngine.fire(rules, facts);
    }
}
