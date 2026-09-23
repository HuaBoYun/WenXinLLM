package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统监控查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMonitorQueryVO extends BaseVo {

    /**
     * 监控类型
     */
    private String monitorType;

    /**
     * 监控项目
     */
    private String monitorItem;

    /**
     * 监控状态
     */
    private String monitorStatus;

    /**
     * 告警级别
     */
    private String alertLevel;

    /**
     * 服务器IP
     */
    private String serverIp;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 监控时间开始
     */
    private String monitorTimeStart;

    /**
     * 监控时间结束
     */
    private String monitorTimeEnd;

    /**
     * 处理状态
     */
    private String processStatus;

    /**
     * 处理人
     */
    private String processBy;
}
