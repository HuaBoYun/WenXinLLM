package com.huabo.contract.config;

import com.huabo.contract.utils.DatabaseCompatibilityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * 数据库兼容性MyBatis拦截器
 * 自动转换SQL语句以适配不同数据库
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Component
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DatabaseCompatibilityInterceptor implements Interceptor {

    @Autowired
    private DatabaseCompatibilityUtils compatibilityUtils;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, 
            SystemMetaObject.DEFAULT_OBJECT_FACTORY, 
            SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, 
            new DefaultReflectorFactory());

        BoundSql boundSql = statementHandler.getBoundSql();
        String originalSql = boundSql.getSql();

        if (originalSql != null && !originalSql.trim().isEmpty()) {
            // 转换SQL以适配当前数据库
            String convertedSql = compatibilityUtils.convertSql(originalSql);
            
            if (!originalSql.equals(convertedSql)) {
                log.debug("SQL转换 - 原始SQL: {}", originalSql);
                log.debug("SQL转换 - 转换后SQL: {}", convertedSql);
                
                // 更新BoundSql中的SQL
                metaObject.setValue("boundSql.sql", convertedSql);
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以通过properties配置拦截器参数
    }
}


