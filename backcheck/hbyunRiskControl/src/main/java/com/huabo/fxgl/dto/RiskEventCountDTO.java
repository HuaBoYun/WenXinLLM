package com.huabo.fxgl.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 风险事件数 DTO
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Data
public class RiskEventCountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发生部门
     */
    private String occurredDepartment;

    /**
     * 一般事件数
     */
    private Integer category1;

    /**
     * 重大事件数
     */
    private Integer category2;
}

