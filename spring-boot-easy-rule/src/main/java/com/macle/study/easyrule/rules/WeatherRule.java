package com.macle.study.easyrule.rules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "Weather Rule", description = "If it rains, take an umbrella")
public class WeatherRule {

    @Condition
    public boolean itRains() {
        // 定义规则的条件逻辑
        return true; // 假设下雨
    }

    @Action
    public void takeUmbrella() {
        // 定义规则触发的动作
        System.out.println("It rains, take an umbrella!");
    }

    @Priority
    public int getPriority() {
        // 定义规则优先级，值越小优先级越高
        return 1;
    }
}
