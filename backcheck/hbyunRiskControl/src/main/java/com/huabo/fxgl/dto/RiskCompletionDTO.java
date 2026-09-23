package com.huabo.fxgl.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 风险完成情况 DTO
 *
 * @author AI Assistant
 * @since 2025-01-08
 */
@Data
public class RiskCompletionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * GB字段：0-已关闭，1-未关闭
     */
    private Integer gb;

    /**
     * 对应的数量
     */
    private Integer count;
}
