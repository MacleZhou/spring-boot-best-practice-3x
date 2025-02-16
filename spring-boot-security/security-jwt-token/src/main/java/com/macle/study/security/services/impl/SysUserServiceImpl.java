package com.macle.study.security.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macle.study.security.infra.repository.entity.SysUser;
import com.macle.study.security.infra.repository.mapper.SysUserMapper;
import com.macle.study.security.services.SysUserService;
import org.springframework.stereotype.Service;


@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
