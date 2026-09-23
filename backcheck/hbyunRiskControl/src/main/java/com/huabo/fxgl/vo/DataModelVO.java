package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据模型返回VO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="DataModelVO", description="数据模型返回对象")
public class DataModelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "模型ID")
    private String modelId;

    @Schema(name = "模型编码")
    private String modelCode;

    @Schema(name = "模型名称")
    private String modelName;

    @Schema(name = "模型类型")
    private String modelType;

    @Schema(name = "模型类型名称")
    private String modelTypeName;

    @Schema(name = "业务含义")
    private String businessMeaning;

    @Schema(name = "计算逻辑描述")
    private String calculationLogic;

    @Schema(name = "数据源ID")
    private String dataSourceId;

    @Schema(name = "数据源名称")
    private String dataSourceName;

    @Schema(name = "完整SQL语句")
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

    @Schema(name = "关联的SQL模板ID")
    private String templateId;

    @Schema(name = "关联的SQL模板名称")
    private String templateName;

    @Schema(name = "SQL参数配置JSON")
    private String parameterConfig;

    @Schema(name = "模板类型")
    private String templateType;

    @Schema(name = "版本号")
    private String version;

    @Schema(name = "状态")
    private String status;

    @Schema(name = "状态名称")
    private String statusName;

    @Schema(name = "是否启用")
    private String isEnabled;

    @Schema(name = "是否启用名称")
    private String isEnabledName;

    @Schema(name = "执行次数")
    private Integer executionCount;

    @Schema(name = "最后执行时间")
    private LocalDateTime lastExecutionTime;

    @Schema(name = "最后执行结果")
    private String executionResult;

    @Schema(name = "最后执行结果名称")
    private String executionResultName;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "创建人姓名")
    private String createUserName;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    private String updateUser;

    @Schema(name = "更新人姓名")
    private String updateUserName;

    @Schema(name = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "是否可编辑")
    private Boolean canEdit;

    @Schema(name = "是否可删除")
    private Boolean canDelete;

    @Schema(name = "是否可复制")
    private Boolean canCopy;

    @Schema(name = "是否可发布")
    private Boolean canPublish;

    @Schema(name = "是否可归档")
    private Boolean canArchive;

    @Schema(name = "是否可执行")
    private Boolean canExecute;

    @Schema(name = "是否可测试")
    private Boolean canTest;

    @Schema(name = "标签列表")
    private String tags;

    @Schema(name = "热度评分")
    private Double popularityScore;

    @Schema(name = "最近使用时间")
    private LocalDateTime recentUseTime;

    // ===== 来源追踪字段（#TASK-2026-08-01-DATA-MODEL-SYNC）=====

    @Schema(name = "来源类型: MANUAL / COMBINATION_SYNC / TEMPLATE")
    private String sourceType;

    @Schema(name = "来源类型显示名")
    private String sourceTypeName;

    @Schema(name = "来源组合ID")
    private String sourceCombinationId;

    @Schema(name = "来源组合名称")
    private String sourceCombinationName;

    @Schema(name = "来源指标配置ID")
    private String sourceIndicatorConfigId;

    @Schema(name = "来源指标ID")
    private String sourceIndicatorId;

    @Schema(name = "最后同步时间")
    private LocalDateTime lastSyncTime;
}
