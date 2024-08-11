package com.macle.study.deployment.service.impl;

import org.springframework.stereotype.Service;

@Service
public class CalculatorCore {
    public int add(int a, int b) {
        Adder adder = new Adder();
        return adder.add(a, b);
    }
}