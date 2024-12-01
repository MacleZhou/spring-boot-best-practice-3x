package com.atshuo.audit.aop.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 属性值比较，用来字段值的变更
 */
@Data
public class ColumnUpdate implements Serializable {
    /**
     * 属性
     */
    private String column;
    /**
     * 字段更新前的值
     */
    private Object before;
    /**
     * 字段更新后的值
     */
    private Object after;

}