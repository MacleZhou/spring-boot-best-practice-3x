package com.macle.study.security.infra.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
@TableName(value = "sys_user")
public class SysUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String headUrl;

    private Long deptId;

    private Long postId;

    private String description;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
