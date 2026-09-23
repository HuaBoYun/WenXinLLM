package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 滚动预测查询参数DTO
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
public class RollingForecastQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预测任务编号
     */
    private String forecastNo;

    /**
     * 预测任务名称
     */
    private String forecastName;

    /**
     * 预算模型ID
     */
    private String modelId;

    /**
     * 预测类型
     */
    private String forecastType;

    /**
     * 开始期间
     */
    private String startPeriod;

    /**
     * 结束期间
     */
    private String endPeriod;

    /**
     * 预测版本
     */
    private String version;

    /**
     * 状态
     */
    private String status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 租户ID
     */
    private String orgId;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;
}

