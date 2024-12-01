package com.atshuo.audit.aop;

import com.alibaba.fastjson.JSON;
import com.atshuo.audit.aop.dto.AuditLogTag;
import com.atshuo.audit.aop.dto.DomainChangeAction;
import com.atshuo.audit.audit.AuditLogDTO;
import com.atshuo.audit.audit.ThreadAuditService;
import com.atshuo.audit.pojo.AuditLog;
import com.atshuo.audit.util.CompareObjUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Aspect
@Component
public class AuditLogAspect {

    Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    @Autowired
    private ThreadAuditService threadAuditService;

    private static void accept(DomainChangeAction change) {
        List<?> oldObject = change.getOldObject();
        if (CollectionUtils.isEmpty(oldObject)) {
            return;
        }
        List<Map<String, Object>> maps = change.getJdbcTemplate().queryForList(change.getQuerySql());
        change.setNewObject(maps);
    }

    /**
     * <p>
     * 业务方法执行前记录
     * </p>
     *
     * @param auditLogTag AuditLogTag
     * @return void
     */
    @Before("@annotation(auditLogTag)")
    public void beforeDataOperate(JoinPoint joinPoint, AuditLogTag auditLogTag) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AuditLogDTO auditLogDTO = threadAuditService.getAuditLogDTO();
        if (auditLogDTO == null) {
            auditLogDTO = new AuditLogDTO(IdWorker.getTimeId());  // 创建线程DTO,设置惟一请求ID
            threadAuditService.setAuditLogDTO(auditLogDTO); // 放入线程作用域
        }
        // 获取当前线程中审计日志DTO
        String ClassName = methodSignature.getDeclaringTypeName();
        auditLogDTO.setExecuteMethod(ClassName + "#" + joinPoint.getSignature().getName());
        // 如果要全局进行业务审计的话，则切面就不需要对指定的注解进行了，可以根据实体对象上增加注解标记
        auditLogDTO.setModel(auditLogTag.model());
        auditLogDTO.setTag(auditLogTag.model());
        // TODO:  userId可以从上下文中获取，本例没有集成登录，暂无法获取  auditLogDTO.setUserId();

    }

    /**
     * 业务方法执行后记录
     */
    @AfterReturning("@annotation(com.atshuo.audit.aop.dto.AuditLogTag)")
    public void afterDataOperate() {
        try {
            List<DomainChangeAction> domainChanges = threadAuditService.getAuditLogDTO().getDomainChanges();
            if (CollectionUtils.isEmpty(domainChanges)) {
                return;
            }
            domainChanges.forEach(AuditLogAspect::accept);
            this.compareAndTransfer(domainChanges);
        } catch (Exception e) {
            logger.error("获取变更前后内容出错", e);
        }
    }

    /**
     * <p>
     * 对比保存
     * </p>
     *
     * @param list list
     * @return void
     */
    public void compareAndTransfer(List<DomainChangeAction> list) {
        List<AuditLog> auditLogs = new ArrayList<>();
        list.forEach(change -> {
            List<?> oldObject = change.getOldObject();
            List<?> newObject = change.getNewObject();
            // 更新前后数据量，无法对应，不做处理，应该属于逻辑删除。
            if (newObject == null) {
                return;
            }
            if (oldObject == null) {
                return;
            }
            if (oldObject.size() != newObject.size()) {
                return;
            }

            for (int i = 0; i < oldObject.size(); i++) {
                try {
                    String oldDataJson = JSON.toJSONString(oldObject.get(i));
                    String newDataJson = JSON.toJSONString(newObject.get(i));
                    String differenceJson = CompareObjUtil.campareJsonObject(oldDataJson, newDataJson);
                    AuditLog auditLog = new AuditLog();
                    auditLog.setDataChange(differenceJson);
                    auditLog.setTransferData(JSON.toJSONString(change.getTransferData()));
                    auditLog.setTableName(change.getTableName());
                    auditLog.setRequestId(threadAuditService.getAuditLogDTO().getRequestId());
                    auditLog.setId(IdWorker.getId());
                    auditLog.setBeforeValue(oldDataJson);
                    auditLog.setNewValue(newDataJson);
                    auditLog.setExecuteMethod(threadAuditService.getAuditLogDTO().getExecuteMethod());
                    auditLogs.add(auditLog);
                } catch (Exception e) {
                    logger.error("解析变更封装审计日志对象时出错", e);
                }
            }
        });
        logger.info("要保存的操作记录数据:{}", JSON.toJSONString(auditLogs));
        // TODO:  可以保存审计日志到数据库，也可以将审计日志放到MQ中，异步消费， 或者将该审计模块封装成starter，引入使用，并通过feign接口导步保存处理，等，
        //  具体根据使用场景进行处理。
        threadAuditService.clear();
    }

}