package com.atshuo.audit.service.impl;

import com.atshuo.audit.mapper.AuditMapper;
import com.atshuo.audit.pojo.AuditLog;
import com.atshuo.audit.service.AuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("auditService")
public class AuditServiceImpl extends ServiceImpl<AuditMapper, AuditLog> implements AuditService {

}
