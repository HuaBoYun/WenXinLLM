package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据模型表
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_DATA_MODEL")
@Schema(name="TblDataModel对象", description="数据模型表")
public class TblDataModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "模型ID")
    @TableId(value = "MODEL_ID", type = IdType.ASSIGN_ID)
    private String modelId;

    @Schema(name = "模型编码")
    @TableField("MODEL_CODE")
    private String modelCode;

    @Schema(name = "模型名称")
    @TableField("MODEL_NAME")
    private String modelName;

    @Schema(name = "模型类型(FINANCIAL/RISK/AUDIT)")
    @TableField("MODEL_TYPE")
    private String modelType;

    @Schema(name = "业务含义")
    @TableField("BUSINESS_MEANING")
    private String businessMeaning;

    @Schema(name = "计算逻辑描述")
    @TableField("CALCULATION_LOGIC")
    private String calculationLogic;

    @Schema(name = "数据源ID")
    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;

    @Schema(name = "完整SQL语句")
    @TableField("SQL_STATEMENT")
    private String sqlStatement;

    @Schema(name = "WITH子句内容")
    @TableField("WITH_CLAUSE")
    private String withClause;

    @Schema(name = "SELECT子句内容")
    @TableField("SELECT_CLAUSE")
    private String selectClause;

    @Schema(name = "FROM子句内容")
    @TableField("FROM_CLAUSE")
    private String fromClause;

    @Schema(name = "WHERE子句内容")
    @TableField("WHERE_CLAUSE")
    private String whereClause;

    @Schema(name = "GROUP BY子句内容")
    @TableField("GROUP_BY_CLAUSE")
    private String groupByClause;

    @Schema(name = "HAVING子句内容")
    @TableField("HAVING_CLAUSE")
    private String havingClause;

    @Schema(name = "ORDER BY子句内容")
    @TableField("ORDER_BY_CLAUSE")
    private String orderByClause;

    @Schema(name = "拖拽配置JSON")
    @TableField("DRAG_CONFIG")
    private String dragConfig;

    @Schema(name = "阈值配置JSON")
    @TableField("THRESHOLD_CONFIG")
    private String thresholdConfig;

    @Schema(name = "预警配置JSON")
    @TableField("WARNING_CONFIG")
    private String warningConfig;

    @Schema(name = "关联的SQL模板ID")
    @TableField("TEMPLATE_ID")
    private String templateId;

    @Schema(name = "SQL参数配置JSON")
    @TableField("PARAMETER_CONFIG")
    private String parameterConfig;

    @Schema(name = "模板类型")
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @Schema(name = "版本号")
    @TableField("VERSION")
    private String version;

    @Schema(name = "状态(DRAFT/TESTING/PUBLISHED)")
    @TableField("STATUS")
    private String status;

    @Schema(name = "是否启用(Y/N)")
    @TableField("IS_ENABLED")
    private String isEnabled;

    @Schema(name = "执行次数")
    @TableField("EXECUTION_COUNT")
    private Integer executionCount;

    @Schema(name = "最后执行时间")
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    @Schema(name = "最后执行结果(SUCCESS/FAILED)")
    @TableField("EXECUTION_RESULT")
    private String executionResult;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    // ===== 来源追踪字段（#TASK-2026-08-01-DATA-MODEL-SYNC）=====

    @Schema(name = "来源类型: MANUAL 手动 / COMBINATION_SYNC 组合同步 / TEMPLATE 模板生成")
    @TableField("SOURCE_TYPE")
    private String sourceType;

    @Schema(name = "来源组合ID (仅 COMBINATION_SYNC 有值)")
    @TableField("SOURCE_COMBINATION_ID")
    private String sourceCombinationId;

    @Schema(name = "来源指标配置ID (TBL_COMBINATION_INDICATOR.CONFIG_ID, 用作幂等键)")
    @TableField("SOURCE_INDICATOR_CONFIG_ID")
    private String sourceIndicatorConfigId;

    @Schema(name = "来源指标ID")
    @TableField("SOURCE_INDICATOR_ID")
    private String sourceIndicatorId;

    @Schema(name = "最后同步时间")
    @TableField("LAST_SYNC_TIME")
    private LocalDateTime lastSyncTime;
}
