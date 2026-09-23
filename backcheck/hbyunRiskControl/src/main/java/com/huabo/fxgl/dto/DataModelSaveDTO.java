package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据模型保存参数DTO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="DataModelSaveDTO", description="数据模型保存参数")
public class DataModelSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "模型ID(更新时必填)")
    private String modelId;

    @Schema(name = "模型编码", required = true)
    private String modelCode;

    @Schema(name = "模型名称", required = true)
    private String modelName;

    @Schema(name = "模型类型", required = true)
    private String modelType;

    @Schema(name = "业务含义")
    private String businessMeaning;

    @Schema(name = "计算逻辑描述")
    private String calculationLogic;

    @Schema(name = "数据源ID", required = true)
    private String dataSourceId;

    @Schema(name = "完整SQL语句", required = true)
    private String sqlStatement;

    @Schema(name = "WITH子句内容")
    private String withClause;

    @Schema(name = "SELECT子句内容")
    private String selectClause;

    @Schema(name = "FROM子句内容")
    private String fromClause;

    @Schema(name = "WHERE子句内容")
    private String whereClause;

    @Schema(name = "GROUP BY子句内容")
    private String groupByClause;

    @Schema(name = "HAVING子句内容")
    private String havingClause;

    @Schema(name = "ORDER BY子句内容")
    private String orderByClause;

    @Schema(name = "拖拽配置JSON")
    private String dragConfig;

    @Schema(name = "阈值配置JSON")
    private String thresholdConfig;

    @Schema(name = "预警配置JSON")
    private String warningConfig;

    @Schema(name = "模板类型")
    private String templateType;

    @Schema(name = "版本号")
    private String version;

    @Schema(name = "状态(DRAFT/PUBLISHED/ARCHIVED)")
    private String status;

    @Schema(name = "是否启用(Y/N)")
    private String isEnabled;

    @Schema(name = "关联的SQL模板ID")
    private String templateId;

    @Schema(name = "SQL参数配置JSON")
    private String parameterConfig;

    @Schema(name = "变更说明")
    private String changeDescription;
}
