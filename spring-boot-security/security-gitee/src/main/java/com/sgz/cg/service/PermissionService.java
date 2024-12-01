package com.sgz.cg.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgz.cg.entry.Permission;
import com.sgz.cg.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {
}