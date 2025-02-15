package com.macle.study.easyrule.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.support.RuleDefinition;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class SpringELTest {
    public static void main(String[] args) throws Exception {
        Rules rules = new Rules();
        // 1、创建Json规则定义读取器
        JsonRuleDefinitionReader jsonRuleDefinitionReader = new JsonRuleDefinitionReader();
        InputStream resourceAsStream = JsonRuleDefinitionReader.class.getResourceAsStream(("/path/rule2.json"));
        if (resourceAsStream == null) return;
        // 2、读取规则定义的json文件
        List<RuleDefinition> ruleDefinitionList = jsonRuleDefinitionReader.read(new InputStreamReader(resourceAsStream));
        for (RuleDefinition ruleDefinition : ruleDefinitionList) {
            // 3、将文件中的规则读取出来后，在通过链式编程创建规则
            RuleBuilder ruleBuilder = new RuleBuilder()
                    .name(ruleDefinition.getName())
                    .priority(ruleDefinition.getPriority())
                    .description(ruleDefinition.getDescription());

            // 4、判断是否满足定义的规则
            ruleBuilder.when(facts -> {
                Boolean result = parseException(ruleDefinition.getCondition(), facts.asMap(), Boolean.class);
                System.out.println("result = " + result);
                return result;
            });

            // 【如果第4步，满足了定义的规则（即返回true），则会进行第5步】
            // 5、获取所有可能需要执行规则
            for (String action : ruleDefinition.getActions()) {
                ruleBuilder.then(facts -> {
                    // 获取最终满足了第4步的执行结果；【如果不满足第4步，则不会执行该操作】
                    String endResult = parseException(action, facts.asMap(), String.class);
                    System.out.println("endResult = " + endResult);
                });
            }
            // 将获取文件中的规则通过RuleBuilder重新创建的规则，注册到规则中
            rules.register(ruleBuilder.build());
        }

        // 创建默认的规则引擎
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Facts facts = new Facts();
        // 通过改变number的值，测试
        facts.put("number", 5);
        // 规则引擎中
        rulesEngine.fire(rules, facts);

    }

    private static <T> T parseException(String expressionStr, Map<String, Object> variables, Class<T> clazz) {
        // 创建一个SpelExpressionParser对象
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expressionStr);

        // 创建一个StandardEvaluationContext对象，用于存储变量和函数
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);

        // 使用Expression对象和EvaluationContext对象计算结果
        T value = expression.getValue(context, clazz);
        return value;
    }
}