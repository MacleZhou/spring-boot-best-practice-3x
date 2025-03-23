package com.macle.study.repository.entity;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.LogicDelete;
import com.easy.query.core.basic.extension.logicdel.LogicDeleteStrategyEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -4834048418175625051L;

    @Column(primaryKey = true)
    private String id;
    /**
     * 创建时间;创建时间
     */
    private LocalDateTime createTime;
    /**
     * Update时间;Update时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人;创建人
     */
    private String createBy;
    /**
     * Update人;Update人
     */
    private String updateBy;
    /**
     * 是否Delete;是否Delete
     */
    @LogicDelete(strategy = LogicDeleteStrategyEnum.BOOLEAN)
    private Boolean deleted;
}