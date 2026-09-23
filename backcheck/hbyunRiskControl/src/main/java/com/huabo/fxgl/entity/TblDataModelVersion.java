package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据模型版本表
 * 
 * @author AI Assistant
 * @since 2025-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_DATA_MODEL_VERSION")
public class TblDataModelVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    @TableId(value = "VERSION_ID", type = IdType.ASSIGN_ID)
    private String versionId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private String versionNo;

    /**
     * 模型编码
     */
    @TableField("MODEL_CODE")
    private String modelCode;

    /**
     * 模型名称
     */
    @TableField("MODEL_NAME")
    private String modelName;

    /**
     * 模型类型
     */
    @TableField("MODEL_TYPE")
    private String modelType;

    /**
     * 业务含义
     */
    @TableField("BUSINESS_MEANING")
    private String businessMeaning;

    /**
     * 计算逻辑描述
     */
    @TableField("CALCULATION_LOGIC")
    private String calculationLogic;

    /**
     * 数据源ID
     */
    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;

    /**
     * 完整SQL语句
     */
    @TableField("SQL_STATEMENT")
    private String sqlStatement;

    /**
     * WITH子句内容
     */
    @TableField("WITH_CLAUSE")
    private String withClause;

    /**
     * SELECT子句内容
     */
    @TableField("SELECT_CLAUSE")
    private String selectClause;

    /**
     * FROM子句内容
     */
    @TableField("FROM_CLAUSE")
    private String fromClause;

    /**
     * WHERE子句内容
     */
    @TableField("WHERE_CLAUSE")
    private String whereClause;

    /**
     * GROUP BY子句内容
     */
    @TableField("GROUP_BY_CLAUSE")
    private String groupByClause;

    /**
     * HAVING子句内容
     */
    @TableField("HAVING_CLAUSE")
    private String havingClause;

    /**
     * ORDER BY子句内容
     */
    @TableField("ORDER_BY_CLAUSE")
    private String orderByClause;

    /**
     * 拖拽配置JSON
     */
    @TableField("DRAG_CONFIG")
    private String dragConfig;

    /**
     * 阈值配置JSON
     */
    @TableField("THRESHOLD_CONFIG")
    private String thresholdConfig;

    /**
     * 预警配置JSON
     */
    @TableField("WARNING_CONFIG")
    private String warningConfig;

    /**
     * 关联的SQL模板ID
     */
    @TableField("TEMPLATE_ID")
    private String templateId;

    /**
     * 参数配置JSON
     */
    @TableField("PARAMETER_CONFIG")
    private String parameterConfig;

    /**
     * 模板类型
     */
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    /**
     * 版本状态(DRAFT/TESTING/PUBLISHED/ARCHIVED)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 是否启用(Y/N)
     */
    @TableField("IS_ENABLED")
    private String isEnabled;

    /**
     * 是否当前版本(Y/N)
     */
    @TableField("IS_CURRENT")
    private String isCurrent;

    /**
     * 执行次数
     */
    @TableField("EXECUTION_COUNT")
    private Integer executionCount;

    /**
     * 最后执行时间
     */
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    /**
     * 最后执行结果(SUCCESS/FAILED)
     */
    @TableField("EXECUTION_RESULT")
    private String executionResult;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 归档时间
     */
    @TableField("ARCHIVE_TIME")
    private LocalDateTime archiveTime;

    /**
     * 变更说明
     */
    @TableField("CHANGE_DESCRIPTION")
    private String changeDescription;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
