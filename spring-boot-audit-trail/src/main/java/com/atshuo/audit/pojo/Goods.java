package com.atshuo.audit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Goods implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String brand;
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
