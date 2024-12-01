package com.atshuo.audit.aop.dto;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;

@Data
public class DomainChangeAction implements Serializable {

    /**
     * 当前数据变更的流程ID
     */
    private String dataId;

    /**
     * jdbctemplate
     */
    private JdbcTemplate jdbcTemplate;
    /**
     * sqlStatement
     */
    private String querySql;
    /**
     * 表名
     */
    private String tableName;
    /**
     * where 条件
     */
    private String whereSql;
    /**
     * 对应实体类
     */
    private Class<?> entityClass;
    /**
     * 更新前数据
     */
    private List<?> oldObject;
    /**
     * 更新后数据
     */
    private List<?> newObject;

    /**
     * 传递的数据
     */
    private List<?> transferData;

}