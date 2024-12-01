package com.atshuo.audit.mapper;

import com.atshuo.audit.pojo.AuditLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditMapper extends BaseMapper<AuditLog> {
}
