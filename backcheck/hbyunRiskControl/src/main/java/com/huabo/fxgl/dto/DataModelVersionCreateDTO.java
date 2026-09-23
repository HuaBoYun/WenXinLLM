package com.huabo.fxgl.dto;

import lombok.Data;

/**
 * 数据模型版本创建DTO
 *
 * @author AI Assistant
 * @since 2025-09-28
 */
@Data
public class DataModelVersionCreateDTO {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 版本号（可选，不提供则自动生成）
     */
    private String versionNo;

    /**
     * 变更说明
     */
    private String changeDescription;

    /**
     * 是否基于当前版本创建
     */
    private Boolean basedOnCurrent = true;

    /**
     * 基于的版本ID（如果不基于当前版本）
     */
    private String baseVersionId;
}
