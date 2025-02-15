package com.macle.study.easyrule.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
//import org.jeasy.rules.core.RuleListener;
//implements RuleListener
public class CustomRuleListener {

    //@Override
    public boolean beforeEvaluate(Rule rule, Facts facts) {
        System.out.println("Evaluating rule: " + rule.getName());
        return true;
    }

    //@Override
    public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
        System.out.println("Rule evaluated: " + rule.getName() + " - " + evaluationResult);
    }

    //@Override
    public void onSuccess(Rule rule, Facts facts) {
        System.out.println("Rule executed successfully: " + rule.getName());
    }

    //@Override
    public void onFailure(Rule rule, Facts facts, Exception exception) {
        System.out.println("Rule execution failed: " + rule.getName());
    }
}
