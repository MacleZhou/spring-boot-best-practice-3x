package com.macle.study.easyrule.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

/**
 * 编程式规则定义
 * */
public class ProgrammaticHelloWorldRule implements Rule {

    @Override
    public boolean evaluate(Facts facts) {
        return facts.get("enabled");
    }

    @Override
    public void execute(Facts facts) {
        System.out.println("hello world");
    }

    @Override
    public int compareTo(Rule rule) {
        return 0;
    }

    public static void main(String[] args) {
        //定义事实对象
        Facts facts = new Facts();
        facts.put("enabled", true);

        //注册编程式规则
        Rules rules = new Rules();
        rules.register(new ProgrammaticHelloWorldRule());

        //使用默认规则引擎根据事实粗发规则
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
    }
}
