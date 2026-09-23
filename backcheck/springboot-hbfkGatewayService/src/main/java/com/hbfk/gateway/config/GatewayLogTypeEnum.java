package com.hbfk.gateway.config;

/**
 * 网关日志类型枚举
 * <p>定义网关插件日志记录的过滤模式：全部记录、按配置交集、按服务ID、按路径</p>
 *
 * @author hbyun
 */
public enum GatewayLogTypeEnum {
    /**
     * Gateway LogType all
     */
    ALL("all"),
    /**
     * Gateway LogType configure
     */
    CONFIGURE("configure"),
    /**
     * Gateway LogType service
     */
    SERVICE("service"),
    /**
     * Gateway LogType path
     */
    PATH("path"),
    ;

    private String type;

    GatewayLogTypeEnum(String type) {
        this.type = type;
    }

    /**
     * 获取日志类型标识
     *
     * @return 日志类型字符串
     */
    public String getType() {
        return type;
    }
}
