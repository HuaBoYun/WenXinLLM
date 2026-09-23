package com.huabo.financialdata.config.db;

import com.huabo.financialdata.config.annotations.DbSourceTypeField;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author lee
 * @version 1.0.0
 **/
@Component
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class}))
public class BbSourceMetaObjectHandler implements Interceptor {

    @Value("$huabo.dbSourceType:")
    private String dbSourceType;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        // 1. 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        //2. 注解型 - 处理
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            //数据源类型 oracle or mysql
            if (null != field.getAnnotation(DbSourceTypeField.class)) {
                if (StringUtils.isNoneBlank(dbSourceType)) {
                    field.set(parameter, dbSourceType);
                }
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
