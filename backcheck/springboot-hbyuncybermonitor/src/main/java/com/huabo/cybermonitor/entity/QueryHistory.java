package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 查询历史记录实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_QUERY_HISTORY")
public class QueryHistory {

    /**
     * 历史记录ID
     */
    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_UUID)
    private String historyId;

    /**
     * 查询名称
     */
    private String queryName;

    /**
     * 查询类型
     */
    private String queryType;

    /**
     * 查询条件
     */
    private String queryConditions;

    /**
     * 查询SQL
     */
    private String querySql;

    /**
     * 查询结果数量
     */
    private Integer resultCount;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 查询状态
     */
    private String queryStatus;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 查询人
     */
    private String queryBy;

    /**
     * 查询时间
     */
    private LocalDateTime queryTime;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    // 查询类型常量
    public static final String TYPE_ENTERPRISE_SEARCH = "ENTERPRISE_SEARCH";
    public static final String TYPE_FINANCIAL_SEARCH = "FINANCIAL_SEARCH";
    public static final String TYPE_RISK_SEARCH = "RISK_SEARCH";
    public static final String TYPE_ASSET_SEARCH = "ASSET_SEARCH";
    public static final String TYPE_REPORT_GENERATION = "REPORT_GENERATION";
    public static final String TYPE_DATA_EXPORT = "DATA_EXPORT";

    // 查询状态常量
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_TIMEOUT = "TIMEOUT";
    public static final String STATUS_CANCELLED = "CANCELLED";
}
