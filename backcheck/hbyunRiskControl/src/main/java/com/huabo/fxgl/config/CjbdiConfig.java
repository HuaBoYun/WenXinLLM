package com.huabo.fxgl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CJBDI（中船投资企业信息查询平台）配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cjbdi")
public class CjbdiConfig {

    /** 平台基础URL（测试环境含/qysstest，生产环境含/qyss） */
    private String baseUrl = "http://192.0.2.200/qysstest";

    /** 接口调用凭证ticket */
    private String ticket;

    /** 认证用户名（已废弃，请使用 disputeLoginName） */
    private String name;

    /** 认证ID（已废弃，请使用 disputeLoginPassword） */
    private String id;

    /** 授权码（1=已授权） */
    private String authorize = "1";

    /** AES加密密钥（16位） */
    private String aesKey;

    /** 连接超时（毫秒） */
    private int connectTimeout = 10000;

    /** 读取超时（毫秒） */
    private int readTimeout = 30000;

    /** ticket缓存时间（秒），默认20分钟 */
    private int ticketCacheTtl = 1200;

    /**
     * 企业纠纷接口（apiCode=24）专用登录账号
     * 对应接口文档中的 loginName 字段
     */
    private String disputeLoginName;

    /**
     * 企业纠纷接口（apiCode=24）专用登录密码
     * 对应接口文档中的 passWord 字段
     */
    private String disputeLoginPassword;
}

