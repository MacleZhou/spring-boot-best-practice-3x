package com.sgz.cg.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = -1L;

    private Integer id;
    private String name;
    private String url;
    private Integer method;
    private String service;
    private Integer parentId;

}