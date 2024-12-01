package com.atshuo.audit.intercetor;

import com.atshuo.audit.aop.dto.DomainChangeAction;
import com.atshuo.audit.audit.ThreadAuditService;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.TableNameParser;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

/**
 * 业务操作 mybatis 拦截器，拦截所有 update操作。
 * 被 @com.atshuo.audit.aop.AuditLogAspect 中切面切到的方法，有更新操作，会被处理，生成审计日志，并记录数据库
 */
@Slf4j
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "update", args = {Statement.class})})
public class BussinessOperationInterceptor extends MetaO implements Interceptor {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ThreadAuditService threadAuditService;

    @Override
    public Object intercept(Invocation invocation) throws Exception {

        // 判断是否需要记录审计日志，如果AOP没有切到的业务，当前线程中就不存在审计日志对象
        if (threadAuditService.getAuditLogDTO() == null) {
            return invocation.proceed();
        }

        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }

        MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);

        if (stmtMetaObj.hasGetter("delegate")) {
            statement = (Statement) stmtMetaObj.getValue("delegate");
        } else if (stmtMetaObj.hasGetter("stmt.statement")) {
            statement = (Statement) stmtMetaObj.getValue("stmt.statement");
        }

        String originalSql = statement.toString();
        originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
        int index = indexOfSqlStart(originalSql);
        if (index > 0) {
            originalSql = originalSql.substring(index);
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());

        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        if (mappedStatement.getSqlCommandType() != null) {
            try {
                // 获取执行Sql
                String sql = originalSql.replace("where", "WHERE");
                // 使用mybatis-plus 工具解析sql获取表名
                Collection<String> tables = new TableNameParser(sql).tables();
                if (CollectionUtils.isEmpty(tables)) {
                    return invocation.proceed();
                }
                String tableName = tables.iterator().next();
                //更新数据
                if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
                    DomainChangeAction change = new DomainChangeAction();
                    change.setTableName(tableName);
                    change.setJdbcTemplate(jdbcTemplate);
                    // 设置sql用于执行完后查询新数据
                    String selectSql = sql.substring(sql.lastIndexOf("WHERE") + 5);
                    // 同表对同条数据操作多次只进行一次对比
                    if (threadAuditService.getAuditLogDTO().getDomainChanges().stream().anyMatch(c -> tableName.equals(c.getTableName())
                            && selectSql.equals(c.getWhereSql()))) {
                        return invocation.proceed();
                    }
                    change.setWhereSql(selectSql);
                    // 获取请求时object
                    Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
                    change.setTransferData(Arrays.asList(parameterObject));
                    String querySql = "select * from " + tableName + " where " + selectSql;
                    change.setQuerySql(querySql);
                    List<Map<String, Object>> maps = jdbcTemplate.queryForList(querySql);
                    change.setOldObject(maps);
                    change.setEntityClass(parameterObject.getClass());
                    threadAuditService.getAuditLogDTO().getDomainChanges().add(change);
                }
            } catch (Exception e) {
                log.error("获取变更前数据时出错。", e);
            }
        }
        return invocation.proceed();
    }

    /**
     * 获取sql语句开头部分
     *
     * @param sql ignore
     * @return ignore
     */
    private int indexOfSqlStart(String sql) {
        String upperCaseSql = sql.toUpperCase();
        Set<Integer> set = new HashSet<>();
        set.add(upperCaseSql.indexOf("SELECT "));
        set.add(upperCaseSql.indexOf("UPDATE "));
        set.add(upperCaseSql.indexOf("INSERT "));
        set.add(upperCaseSql.indexOf("DELETE "));
        set.remove(-1);
        if (CollectionUtils.isEmpty(set)) {
            return -1;
        }
        List<Integer> list = new ArrayList<>(set);
        list.sort(Comparator.naturalOrder());
        return list.get(0);
    }
}