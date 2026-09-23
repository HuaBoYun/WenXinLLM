package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 评估模型查询DTO
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@Schema(name="EvaluationModelQueryDTO", description="评估模型查询参数")
public class EvaluationModelQueryDTO {

    @Schema(name = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(name = "每页大小", example = "20")
    private Integer pageSize = 20;

    @Schema(name = "模型名称(模糊查询)")
    private String modelName;

    @Schema(name = "业务场景(PROCUREMENT/FINANCE/AUDIT)")
    private String businessScenario;

    @Schema(name = "状态(DRAFT/TESTING/PUBLISHED/ARCHIVED)")
    private String status;

    @Schema(name = "是否启用(Y/N)")
    private String isEnabled;

    @Schema(name = "行业类型")
    private String industryType;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "开始时间(格式: yyyy-MM-dd)")
    private String startTime;

    @Schema(name = "结束时间(格式: yyyy-MM-dd)")
    private String endTime;

    @Schema(name = "排序字段")
    private String sortField = "CREATE_TIME";

    @Schema(name = "排序方向(ASC/DESC)")
    private String sortOrder = "DESC";
}
