package com.atshuo.audit.audit;

import com.atshuo.audit.aop.dto.DomainChangeAction;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AuditLogDTO implements Serializable {
    public AuditLogDTO(String requestId) {
        this.requestId = requestId;
        this.domainChanges = new ArrayList<>();
    }

    /**
     * 标记当前请求ID
     */
    private String requestId;
    /**
     * 变更内容
     */
    private String dataChange;
    /**
     * 原数据值
     */
    private String beforeValue;
    /**
     * 新数据值
     */
    private String newValue;
    /**
     * 操作的表名
     */
    private String tableName;
    /**
     * 业务调用的更新方法
     */
    private String executeMethod;
    /**
     * 传递的值
     */
    private String transferData;
    /**
     * 变更对象
     */
    private List<DomainChangeAction> domainChanges;

    /**
     * 子系统或具体模块， 对应 @com.atshuo.audit.aop.dto.AuditLogDTO.model
     */
    private String model;
    /**
     * 具体业务标记， 对应 @com.atshuo.audit.aop.dto.AuditLogDTO.tag
     */
    private String tag;

    /**
     * 用户ID
     */
    private Long userId;


}
