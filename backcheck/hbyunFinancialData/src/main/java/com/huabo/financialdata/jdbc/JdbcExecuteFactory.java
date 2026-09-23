package com.huabo.financialdata.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.huabo.financialdata.config.exception.BizException;

/**
 * @Author weizhihui
 * @Date 2024/4/25 9:34
 * @Description 功能简要描述
 */
@Component
public class JdbcExecuteFactory {

    @Value("${spring.datasource.oracle.driverClassName}")
    private String currentDriver;

    @Autowired
    private List<JdbcExecuteTemplate> jdbcExecuteTemplateList;

    public JdbcExecuteTemplate getCurrentJdbcExecute() {

        Optional<JdbcExecuteTemplate> optional = jdbcExecuteTemplateList.stream().filter(e -> e.getDriver().equals(currentDriver)).findAny();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new BizException("不支持的数据库类型："+ currentDriver);
    }
}
