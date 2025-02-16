package com.macle.study.security.infra.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macle.study.security.infra.repository.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
