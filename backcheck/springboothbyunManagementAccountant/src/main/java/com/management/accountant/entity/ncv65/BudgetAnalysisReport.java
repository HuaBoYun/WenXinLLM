package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算分析报告实体类
 * 
 * @description 预算分析报告管理实体，支持多维度分析和智能分析功能
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_ANALYSIS_REPORT")
public class BudgetAnalysisReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 报告编码
     */
    @TableField("REPORT_CODE")
    private String reportCode;

    /**
     * 报告名称
     */
    @TableField("REPORT_NAME")
    private String reportName;

    /**
     * 报告类型：variance-差异分析，trend-趋势分析，multidimensional-多维分析，forecast-预测分析
     */
    @TableField("REPORT_TYPE")
    private String reportType;

    /**
     * 分析维度（JSON格式）
     */
    @TableField("ANALYSIS_DIMENSIONS")
    private String analysisDimensions;

    /**
     * 分析指标（JSON格式）
     */
    @TableField("ANALYSIS_INDICATORS")
    private String analysisIndicators;

    /**
     * 分析期间
     */
    @TableField("ANALYSIS_PERIOD")
    private String analysisPeriod;

    /**
     * 分析范围（JSON格式）
     */
    @TableField("ANALYSIS_SCOPE")
    private String analysisScope;

    /**
     * 报告描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 分析结果（JSON格式）
     */
    @TableField("ANALYSIS_RESULT")
    private String analysisResult;

    /**
     * 关键发现（JSON格式）
     */
    @TableField("KEY_FINDINGS")
    private String keyFindings;

    /**
     * 建议措施（JSON格式）
     */
    @TableField("RECOMMENDATIONS")
    private String recommendations;

    /**
     * 风险提示（JSON格式）
     */
    @TableField("RISK_ALERTS")
    private String riskAlerts;

    /**
     * 图表配置（JSON格式）
     */
    @TableField("CHART_CONFIG")
    private String chartConfig;

    /**
     * 报告模板ID
     */
    @TableField("TEMPLATE_ID")
    private String templateId;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 生成方式：manual-手工生成，auto-自动生成，scheduled-定时生成
     */
    @TableField("GENERATION_TYPE")
    private String generationType;

    /**
     * 生成时间
     */
    @TableField("GENERATION_TIME")
    private LocalDateTime generationTime;

    /**
     * 生成人ID
     */
    @TableField("GENERATOR_ID")
    private String generatorId;

    /**
     * 生成人姓名
     */
    @TableField("GENERATOR_NAME")
    private String generatorName;

    /**
     * 报告状态：draft-草稿，generated-已生成，reviewed-已审阅，published-已发布
     */
    @TableField("REPORT_STATUS")
    private String reportStatus;

    /**
     * 审阅人ID
     */
    @TableField("REVIEWER_ID")
    private String reviewerId;

    /**
     * 审阅人姓名
     */
    @TableField("REVIEWER_NAME")
    private String reviewerName;

    /**
     * 审阅时间
     */
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;

    /**
     * 审阅意见
     */
    @TableField("REVIEW_COMMENTS")
    private String reviewComments;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 访问权限配置（JSON格式）
     */
    @TableField("ACCESS_PERMISSIONS")
    private String accessPermissions;

    /**
     * 是否公开
     */
    @TableField("IS_PUBLIC")
    private Boolean isPublic;

    /**
     * 下载次数
     */
    @TableField("DOWNLOAD_COUNT")
    private Integer downloadCount;

    /**
     * 查看次数
     */
    @TableField("VIEW_COUNT")
    private Integer viewCount;

    /**
     * 文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 文件格式：pdf，excel，word，html
     */
    @TableField("FILE_FORMAT")
    private String fileFormat;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 常量定义
    public static final String REPORT_TYPE_VARIANCE = "variance";
    public static final String REPORT_TYPE_TREND = "trend";
    public static final String REPORT_TYPE_MULTIDIMENSIONAL = "multidimensional";
    public static final String REPORT_TYPE_FORECAST = "forecast";

    public static final String GENERATION_TYPE_MANUAL = "manual";
    public static final String GENERATION_TYPE_AUTO = "auto";
    public static final String GENERATION_TYPE_SCHEDULED = "scheduled";

    public static final String REPORT_STATUS_DRAFT = "draft";
    public static final String REPORT_STATUS_GENERATED = "generated";
    public static final String REPORT_STATUS_REVIEWED = "reviewed";
    public static final String REPORT_STATUS_PUBLISHED = "published";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
