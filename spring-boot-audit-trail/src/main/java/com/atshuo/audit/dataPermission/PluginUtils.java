package com.atshuo.audit.dataPermission;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 说明: 从mybatis plus中抽出来主要是为了解决jdk17不兼容 realTarget 方法的问题、减少cpu占用
 */
public abstract class PluginUtils {
    public static final String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";

    /**
     * 获得真正的处理对象,可能多层代理.
     */
    @SuppressWarnings("unchecked")
    public static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }

    /**
     * 给 BoundSql 设置 additionalParameters
     *
     * @param boundSql             BoundSql
     * @param additionalParameters additionalParameters
     */
    public static void setAdditionalParameter(BoundSql boundSql, Map<String, Object> additionalParameters) {
        additionalParameters.forEach(boundSql::setAdditionalParameter);
    }

    public static PluginUtils.MPBoundSql mpBoundSql(BoundSql boundSql) {
        return new PluginUtils.MPBoundSql(boundSql);
    }

    public static PluginUtils.MPStatementHandler mpStatementHandler(StatementHandler statementHandler) {
        statementHandler = realTarget(statementHandler);
        MetaObject object = SystemMetaObject.forObject(statementHandler);
        return new PluginUtils.MPStatementHandler(SystemMetaObject.forObject(object.getValue("delegate")));
    }

    /**
     * {@link org.apache.ibatis.executor.statement.BaseStatementHandler}
     */
    public static class MPStatementHandler {
        private final MetaObject statementHandler;

        MPStatementHandler(MetaObject statementHandler) {
            this.statementHandler = statementHandler;
        }

        public ParameterHandler parameterHandler() {
            return get("parameterHandler");
        }

        public MappedStatement mappedStatement() {
            return get("mappedStatement");
        }

        public Executor executor() {
            return get("executor");
        }

        public PluginUtils.MPBoundSql mPBoundSql() {
            return new PluginUtils.MPBoundSql(boundSql());
        }

        public BoundSql boundSql() {
            return get("boundSql");
        }

        public Configuration configuration() {
            return get("configuration");
        }

        @SuppressWarnings("unchecked")
        private <T> T get(String property) {
            return (T) statementHandler.getValue(property);
        }
    }

    /**
     * {@link BoundSql}
     */
    public static class MPBoundSql {
        private final MetaObject boundSql;
        private final BoundSql delegate;

        MPBoundSql(BoundSql boundSql) {
            this.delegate = boundSql;
            this.boundSql = SystemMetaObject.forObject(boundSql);
        }

        public String sql() {
            return delegate.getSql();
        }

        public void sql(String sql) {
            boundSql.setValue("sql", sql);
        }

        public List<ParameterMapping> parameterMappings() {
            List<ParameterMapping> parameterMappings = delegate.getParameterMappings();
            return new ArrayList<>(parameterMappings);
        }

        public void parameterMappings(List<ParameterMapping> parameterMappings) {
            boundSql.setValue("parameterMappings", Collections.unmodifiableList(parameterMappings));
        }

        public Object parameterObject() {
            return get("parameterObject");
        }

        public Map<String, Object> additionalParameters() {
            return get("additionalParameters");
        }

        @SuppressWarnings("unchecked")
        private <T> T get(String property) {
            return (T) boundSql.getValue(property);
        }
    }
}