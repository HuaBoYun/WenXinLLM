package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据模型查询参数DTO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="DataModelQueryDTO", description="数据模型查询参数")
public class DataModelQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(name = "每页大小", example = "20")
    private Integer pageSize = 20;

    @Schema(name = "模型名称(模糊查询)")
    private String modelName;

    @Schema(name = "模型编码")
    private String modelCode;

    @Schema(name = "模型类型(FINANCIAL/RISK/AUDIT)")
    private String modelType;

    @Schema(name = "数据源ID")
    private String dataSourceId;

    @Schema(name = "状态(DRAFT/PUBLISHED/ARCHIVED)")
    private String status;

    @Schema(name = "是否启用(Y/N)")
    private String isEnabled;

    @Schema(name = "模板类型")
    private String templateType;

    @Schema(name = "版本号")
    private String version;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "开始时间")
    private String startTime;

    @Schema(name = "结束时间")
    private String endTime;

    @Schema(name = "排序字段")
    private String sortField;

    @Schema(name = "排序方向(ASC/DESC)")
    private String sortOrder;

    @Schema(name = "业务含义(模糊查询)")
    private String businessMeaning;

    @Schema(name = "最小执行次数")
    private Integer minExecutionCount;

    @Schema(name = "最大执行次数")
    private Integer maxExecutionCount;

    @Schema(name = "最后执行结果(SUCCESS/FAILED)")
    private String executionResult;

    @Schema(name = "来源类型(MANUAL/COMBINATION_SYNC/TEMPLATE)")
    private String sourceType;

    @Schema(name = "来源组合ID")
    private String sourceCombinationId;
}
