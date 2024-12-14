package com.macle.study.spel;

import com.google.common.collect.Maps;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;

public class Temp {

    public static void main(String[] args) {

        ExpressionParser parser = new SpelExpressionParser();

        // 计算得数
        Expression expression = parser.parseExpression("1+1");
        Object result = expression.getValue();
        System.out.println(result);

        // 使用 java api
        Expression expression1 = parser.parseExpression("T(java.lang.Math).random()");
        Object result1 = expression1.getValue();
        System.out.println(result1);

        // 补充占位
        ParserContext templateParserContext = new TemplateParserContext();
        Expression expression2 = parser.parseExpression("小明今年的年龄是：#{[X]}", templateParserContext);
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("X", 10);
        String result2 = expression2.getValue(paramMap, String.class);
        System.out.println(result2);
    }
}
