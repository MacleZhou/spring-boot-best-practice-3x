package com.macle.study.deployment.service.impl;

import com.macle.study.deployment.service.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("calculator")
public class CalculatorImpl implements Calculator {
    @Autowired
    CalculatorCore calculatorCore;
    /**
     * 注解方式
     */
    @Override
    public int calculate(int a, int b) {
        int c = calculatorCore.add(a, b);
        return c;
    }
    /**
     * 反射方式
     */
    @Override
    public int add(int a, int b) {
        return new CalculatorCore().add(a, b);
    }
}