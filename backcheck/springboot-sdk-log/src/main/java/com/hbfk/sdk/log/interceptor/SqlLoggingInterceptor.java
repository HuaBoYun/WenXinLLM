package com.hbfk.sdk.log.interceptor;

import com.hbfk.sdk.log.context.SqlLogHolder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Author: 61
 */
@Intercepts({
        // 拦截 prepare 方法
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        // 拦截 query 方法
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        // 拦截 update 方法
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class})
})
public class SqlLoggingInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        // 获取原始 SQL 语句
        String sql = boundSql.getSql();

        // 获取参数对象
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 替换 SQL 中的 "?" 占位符
        String formattedSql = formatSqlWithParameters(sql, parameterObject, parameterMappings);
        // 打印格式化后的 SQL
        System.out.println("Formatted SQL: " + formattedSql);
        SqlLogHolder.addLog(formattedSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    // 方法：替换 SQL 中的 "?" 占位符
    private String formatSqlWithParameters(String sql, Object parameterObject, List<ParameterMapping> parameterMappings) {
        StringBuilder formattedSql = new StringBuilder(sql);

        if (parameterObject != null && parameterMappings != null && !parameterMappings.isEmpty()) {
            int paramIndex = 0;
            for (ParameterMapping param : parameterMappings) {
                String property = param.getProperty();
                Object value = getParameterValue(parameterObject, property);
                String valueStr = (value == null) ? "NULL" : value.toString();
                // 替换 "?" 为参数值
                formattedSql = new StringBuilder(formattedSql.toString().replaceFirst("\\?", valueStr));
            }
        }
        return formattedSql.toString();
    }

    // 方法：通过反射获取参数对象的字段值
    private Object getParameterValue(Object parameterObject, String fieldName) {
        try {
            Field field = parameterObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(parameterObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        System.out.println("----------------------拦截器进来了--------");
//        Object target = invocation.getTarget();
//        StatementHandler statementHandler = (StatementHandler) unwrapTarget(target);
//        String sql = statementHandler.getBoundSql().getSql();
//        SqlLogHolder.addLog(sql.trim());
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//        // No properties needed
//    }
//
//    private Object unwrapTarget(Object target) {
//        while (Proxy.isProxyClass(target.getClass())) {
//            target = Proxy.getInvocationHandler(target);
//        }
//        return target;
//    }
}
