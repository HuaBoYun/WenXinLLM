package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据质量检查查询参数DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class DataQualityCheckQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查编码
     */
    private String checkCode;

    /**
     * 检查名称
     */
    private String checkName;

    /**
     * 检查类型
     */
    private String checkType;

    /**
     * 检查对象类型
     */
    private String targetType;

    /**
     * 检查对象ID
     */
    private Long targetId;

    /**
     * 检查状态
     */
    private String checkStatus;

    /**
     * 质量等级
     */
    private String qualityLevel;

    /**
     * 开始时间（开始）
     */
    private String startTimeBegin;

    /**
     * 开始时间（结束）
     */
    private String startTimeEnd;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

