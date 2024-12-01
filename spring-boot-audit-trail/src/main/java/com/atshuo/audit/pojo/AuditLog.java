package com.atshuo.audit.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("audit-log")
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    @TableId
    private Long id;

    /**
     * 标记当前请求ID
     */
    @TableField("request_id")
    private String requestId;

    /**
     * 变更内容
     */
    @TableField("data_change")
    private String dataChange;

    /**
     * 原数据值
     */
    @TableField("before_value")
    private String beforeValue;

    /**
     * 新数据值
     */
    @TableField("new_value")
    private String newValue;

    /**
     * 操作的表名
     */
    @TableField("table_name")
    private String tableName;

    /**
     * 业务调用的更新方法
     */
    @TableField("execute_method")
    private String executeMethod;

    /**
     * 传递的值
     */
    private String transferData;

}