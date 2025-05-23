package com.sgz.cg.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_role")
public class Role implements Serializable {
    private static final long serialVersionUID = -1L;

    private Integer id;
    private String name;
    private String remark;
}