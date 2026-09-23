package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 执行质量检查参数DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class ExecuteQualityCheckParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查名称
     */
    private String checkName;

    /**
     * 检查类型：MANUAL(手动)/AUTO(自动)
     */
    private String checkType;

    /**
     * 检查对象类型：TASK(归集任务)/TABLE(数据表)/FIELD(字段)
     */
    private String targetType;

    /**
     * 检查对象ID
     */
    private Long targetId;

    /**
     * 检查对象名称
     */
    private String targetName;

    /**
     * 规则ID列表
     */
    private List<Long> ruleIds;
}

