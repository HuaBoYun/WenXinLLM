package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算报告实体类
 * 
 * @description 预算报告管理，支持多种报告类型的生成、发布和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_REPORT")
public class BudgetReport implements Serializable {

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
     * 报告类型
     */
    @TableField("REPORT_TYPE")
    private String reportType;

    /**
     * 报告分类
     */
    @TableField("REPORT_CATEGORY")
    private String reportCategory;

    /**
     * 报告模板ID
     */
    @TableField("TEMPLATE_ID")
    private String templateId;

    /**
     * 报告模板名称
     */
    @TableField("TEMPLATE_NAME")
    private String templateName;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 报告期间
     */
    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    private LocalDateTime endDate;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 组织层级
     */
    @TableField("ORGANIZATION_LEVEL")
    private Integer organizationLevel;

    /**
     * 报告范围
     */
    @TableField("REPORT_SCOPE")
    private String reportScope;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 报告格式
     */
    @TableField("REPORT_FORMAT")
    private String reportFormat;

    /**
     * 报告语言
     */
    @TableField("REPORT_LANGUAGE")
    private String reportLanguage;

    /**
     * 报告状态
     */
    @TableField("REPORT_STATUS")
    private String reportStatus;

    /**
     * 生成状态
     */
    @TableField("GENERATION_STATUS")
    private String generationStatus;

    /**
     * 审批状态
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 发布状态
     */
    @TableField("PUBLISH_STATUS")
    private String publishStatus;

    /**
     * 生成时间
     */
    @TableField("GENERATION_TIME")
    private LocalDateTime generationTime;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

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
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 发布人ID
     */
    @TableField("PUBLISHER_ID")
    private String publisherId;

    /**
     * 发布人姓名
     */
    @TableField("PUBLISHER_NAME")
    private String publisherName;

    /**
     * 报告文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 报告文件名
     */
    @TableField("FILE_NAME")
    private String fileName;

    /**
     * 文件大小
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 文件MD5
     */
    @TableField("FILE_MD5")
    private String fileMd5;

    /**
     * 报告页数
     */
    @TableField("PAGE_COUNT")
    private Integer pageCount;

    /**
     * 数据行数
     */
    @TableField("DATA_ROW_COUNT")
    private Integer dataRowCount;

    /**
     * 生成耗时（秒）
     */
    @TableField("GENERATION_DURATION")
    private Integer generationDuration;

    /**
     * 报告摘要
     */
    @TableField("REPORT_SUMMARY")
    private String reportSummary;

    /**
     * 关键指标
     */
    @TableField("KEY_INDICATORS")
    private String keyIndicators;

    /**
     * 主要发现
     */
    @TableField("KEY_FINDINGS")
    private String keyFindings;

    /**
     * 建议措施
     */
    @TableField("RECOMMENDATIONS")
    private String recommendations;

    /**
     * 风险提示
     */
    @TableField("RISK_ALERTS")
    private String riskAlerts;

    /**
     * 是否自动生成
     */
    @TableField("IS_AUTO_GENERATION")
    private Boolean isAutoGeneration;

    /**
     * 是否定期生成
     */
    @TableField("IS_SCHEDULED")
    private Boolean isScheduled;

    /**
     * 是否公开报告
     */
    @TableField("IS_PUBLIC")
    private Boolean isPublic;

    /**
     * 是否机密报告
     */
    @TableField("IS_CONFIDENTIAL")
    private Boolean isConfidential;

    /**
     * 生成频率
     */
    @TableField("GENERATION_FREQUENCY")
    private String generationFrequency;

    /**
     * 下次生成时间
     */
    @TableField("NEXT_GENERATION_TIME")
    private LocalDateTime nextGenerationTime;

    /**
     * 订阅人员列表
     */
    @TableField("SUBSCRIBERS")
    private String subscribers;

    /**
     * 分发列表
     */
    @TableField("DISTRIBUTION_LIST")
    private String distributionList;

    /**
     * 访问权限
     */
    @TableField("ACCESS_PERMISSIONS")
    private String accessPermissions;

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
     * 最后访问时间
     */
    @TableField("LAST_ACCESS_TIME")
    private LocalDateTime lastAccessTime;

    /**
     * 有效期开始时间
     */
    @TableField("VALID_FROM")
    private LocalDateTime validFrom;

    /**
     * 有效期结束时间
     */
    @TableField("VALID_TO")
    private LocalDateTime validTo;

    /**
     * 归档时间
     */
    @TableField("ARCHIVE_TIME")
    private LocalDateTime archiveTime;

    /**
     * 报告配置
     */
    @TableField("REPORT_CONFIG")
    private String reportConfig;

    /**
     * 数据筛选条件
     */
    @TableField("DATA_FILTERS")
    private String dataFilters;

    /**
     * 排序规则
     */
    @TableField("SORT_RULES")
    private String sortRules;

    /**
     * 分组规则
     */
    @TableField("GROUP_RULES")
    private String groupRules;

    /**
     * 汇总规则
     */
    @TableField("SUMMARY_RULES")
    private String summaryRules;

    /**
     * 格式化规则
     */
    @TableField("FORMAT_RULES")
    private String formatRules;

    /**
     * 生成日志
     */
    @TableField("GENERATION_LOG")
    private String generationLog;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 质量评分
     */
    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    /**
     * 完整性检查结果
     */
    @TableField("COMPLETENESS_CHECK")
    private String completenessCheck;

    /**
     * 准确性检查结果
     */
    @TableField("ACCURACY_CHECK")
    private String accuracyCheck;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 版本号
     */
    @Version
    @TableField("VERSION")
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

    // ==================== 常量定义 ====================

    /**
     * 报告类型常量
     */
    public static final String REPORT_TYPE_BUDGET_SUMMARY = "budget_summary";
    public static final String REPORT_TYPE_EXECUTION_ANALYSIS = "execution_analysis";
    public static final String REPORT_TYPE_VARIANCE_ANALYSIS = "variance_analysis";
    public static final String REPORT_TYPE_FORECAST_REPORT = "forecast_report";
    public static final String REPORT_TYPE_PERFORMANCE_REPORT = "performance_report";
    public static final String REPORT_TYPE_DASHBOARD = "dashboard";
    public static final String REPORT_TYPE_CUSTOM = "custom";

    /**
     * 报告分类常量
     */
    public static final String REPORT_CATEGORY_MANAGEMENT = "management";
    public static final String REPORT_CATEGORY_FINANCIAL = "financial";
    public static final String REPORT_CATEGORY_OPERATIONAL = "operational";
    public static final String REPORT_CATEGORY_REGULATORY = "regulatory";
    public static final String REPORT_CATEGORY_INTERNAL = "internal";

    /**
     * 报告格式常量
     */
    public static final String REPORT_FORMAT_PDF = "pdf";
    public static final String REPORT_FORMAT_EXCEL = "excel";
    public static final String REPORT_FORMAT_WORD = "word";
    public static final String REPORT_FORMAT_HTML = "html";
    public static final String REPORT_FORMAT_CSV = "csv";

    /**
     * 报告状态常量
     */
    public static final String REPORT_STATUS_DRAFT = "draft";
    public static final String REPORT_STATUS_GENERATING = "generating";
    public static final String REPORT_STATUS_COMPLETED = "completed";
    public static final String REPORT_STATUS_FAILED = "failed";
    public static final String REPORT_STATUS_ARCHIVED = "archived";

    /**
     * 生成状态常量
     */
    public static final String GENERATION_STATUS_PENDING = "pending";
    public static final String GENERATION_STATUS_PROCESSING = "processing";
    public static final String GENERATION_STATUS_SUCCESS = "success";
    public static final String GENERATION_STATUS_FAILED = "failed";

    /**
     * 审批状态常量
     */
    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";

    /**
     * 发布状态常量
     */
    public static final String PUBLISH_STATUS_UNPUBLISHED = "unpublished";
    public static final String PUBLISH_STATUS_PUBLISHED = "published";
    public static final String PUBLISH_STATUS_ARCHIVED = "archived";

    /**
     * 生成频率常量
     */
    public static final String GENERATION_FREQUENCY_DAILY = "daily";
    public static final String GENERATION_FREQUENCY_WEEKLY = "weekly";
    public static final String GENERATION_FREQUENCY_MONTHLY = "monthly";
    public static final String GENERATION_FREQUENCY_QUARTERLY = "quarterly";
    public static final String GENERATION_FREQUENCY_YEARLY = "yearly";
    public static final String GENERATION_FREQUENCY_MANUAL = "manual";
}
