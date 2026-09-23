package com.huabo.financialdata.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置统一管理
 *
 * @author lee
 */
@Getter
@Component
public class GlobalConfig {

    @Value("${huabo.dbSourceType:mysql}")
    private String dbSourceType;

}
