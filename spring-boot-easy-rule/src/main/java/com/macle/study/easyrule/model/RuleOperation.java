package com.macle.study.easyrule.model;

public enum RuleOperation {

    OPERATOR_GREATER_THEN(">"),
    OPERATOR_LESS_THEN("<"),
    OPERATOR_EQUAL("=="),
    OPERATOR_UNQEUAL("!="),
    OPERATOR_CONTAIN("contains"),
    OPERATOR_PREFIX("startWith"),
    OPERATOR_SUFFIX("endWith"),
    INPUT_VALUE("input"),
    FIELD_VALUE("field"),
    LINK_AND("&&"),
    LINK_OR("||");

    private String operator;

    RuleOperation(String operator) {
        this.operator = operator;
    }


}
