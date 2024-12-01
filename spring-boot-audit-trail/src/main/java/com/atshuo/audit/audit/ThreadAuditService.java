package com.atshuo.audit.audit;


import org.springframework.stereotype.Service;


/**
 * 线程绑定服务类
 */
@Service
public class ThreadAuditService {
    /**
     * 封装当前线程请求信息
     */
    private final ThreadLocal<AuditLogDTO> auditLogDTO = new ThreadLocal<>();


    public AuditLogDTO getAuditLogDTO() {
        return auditLogDTO.get();
    }

    public void setAuditLogDTO(AuditLogDTO auditLogDTO) {
        this.auditLogDTO.set(auditLogDTO);
    }

    /**
     * 清除当前线程中的审计日志对象
     */
    public void clear() {
        auditLogDTO.remove();
    }

}