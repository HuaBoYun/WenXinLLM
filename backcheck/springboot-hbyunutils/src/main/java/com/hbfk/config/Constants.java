package com.hbfk.config;

/**
 * 通用常量信息
 *
 * @author 第三方组件
 */
public class Constants {

    /**
     * token
     */
    public static final String AUTHORIZATION = "token";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌有效期（分钟）
     */
    public final static long TOKEN_EXPIRE = 720;

    /**
     * swagger版本号
     */
    public final static String SWAGGER_VERSION = "3.1.0";

    /**
     * swagger版本号
     */
    public final static String USER_AGENT = "User-Agent";
    
    /**
     * 定义Redis缓存默认过期时间
     */
    public static final int CACHE_TIMEOUT_HOUR=2;


    /**
     * 定义unknown字串串的常量
     */
    public  static final String UNKNOWN = "unknown";

    /**
     * 定义MB的计算常量
     */
    public static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    public static final int KB = 1024;
    
}
