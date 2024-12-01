package com.sgz.cg.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgz.cg.entry.Role;
import com.sgz.cg.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
}