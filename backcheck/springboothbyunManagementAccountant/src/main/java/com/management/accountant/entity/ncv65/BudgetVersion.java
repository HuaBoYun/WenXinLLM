package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算版本实体类
 * 
 * @description 预算版本管理实体，支持预算数据的版本控制和历史追溯
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_VERSION")
public class BudgetVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 版本编码
     */
    @TableField("VERSION_CODE")
    private String versionCode;

    /**
     * 版本名称
     */
    @TableField("VERSION_NAME")
    private String versionName;

    /**
     * 版本简称
     */
    @TableField("VERSION_SHORT_NAME")
    private String versionShortName;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 预算模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 场景ID
     */
    @TableField("SCENARIO_ID")
    private String scenarioId;

    /**
     * 版本类型：initial-初始版本，adjustment-调整版本，revision-修订版本，final-最终版本
     */
    @TableField("VERSION_TYPE")
    private String versionType;

    /**
     * 版本号（格式：v1.0.0）
     */
    @TableField("VERSION_NUMBER")
    private String versionNumber;

    /**
     * 主版本号
     */
    @TableField("MAJOR_VERSION")
    private Integer majorVersion;

    /**
     * 次版本号
     */
    @TableField("MINOR_VERSION")
    private Integer minorVersion;

    /**
     * 修订版本号
     */
    @TableField("PATCH_VERSION")
    private Integer patchVersion;

    /**
     * 父版本ID
     */
    @TableField("PARENT_VERSION_ID")
    private String parentVersionId;

    /**
     * 基准版本ID
     */
    @TableField("BASE_VERSION_ID")
    private String baseVersionId;

    /**
     * 版本描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 变更说明
     */
    @TableField("CHANGE_LOG")
    private String changeLog;

    /**
     * 变更原因
     */
    @TableField("CHANGE_REASON")
    private String changeReason;

    /**
     * 变更范围（JSON格式）
     */
    @TableField("CHANGE_SCOPE")
    private String changeScope;

    /**
     * 变更影响分析（JSON格式）
     */
    @TableField("IMPACT_ANALYSIS")
    private String impactAnalysis;

    /**
     * 是否当前版本
     */
    @TableField("IS_CURRENT")
    private Boolean isCurrent;

    /**
     * 是否基准版本
     */
    @TableField("IS_BASELINE")
    private Boolean isBaseline;

    /**
     * 是否最终版本
     */
    @TableField("IS_FINAL")
    private Boolean isFinal;

    /**
     * 是否已发布
     */
    @TableField("IS_PUBLISHED")
    private Boolean isPublished;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    @TableField("PUBLISHED_BY")
    private String publishedBy;

    /**
     * 发布人姓名
     */
    @TableField("PUBLISHED_BY_NAME")
    private String publishedByName;

    /**
     * 是否锁定
     */
    @TableField("IS_LOCKED")
    private Boolean isLocked;

    /**
     * 锁定时间
     */
    @TableField("LOCK_TIME")
    private LocalDateTime lockTime;

    /**
     * 锁定人ID
     */
    @TableField("LOCKED_BY")
    private String lockedBy;

    /**
     * 锁定人姓名
     */
    @TableField("LOCKED_BY_NAME")
    private String lockedByName;

    /**
     * 锁定原因
     */
    @TableField("LOCK_REASON")
    private String lockReason;

    /**
     * 审批状态：draft-草稿，submitted-已提交，approved-已审批，rejected-已拒绝
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

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
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

    /**
     * 数据统计信息（JSON格式）
     */
    @TableField("DATA_STATISTICS")
    private String dataStatistics;

    /**
     * 数据完整性检查结果（JSON格式）
     */
    @TableField("DATA_INTEGRITY_CHECK")
    private String dataIntegrityCheck;

    /**
     * 数据质量评分
     */
    @TableField("QUALITY_SCORE")
    private Integer qualityScore;

    /**
     * 版本标签（JSON格式）
     */
    @TableField("VERSION_TAGS")
    private String versionTags;

    /**
     * 版本属性（JSON格式）
     */
    @TableField("VERSION_ATTRIBUTES")
    private String versionAttributes;

    /**
     * 备份信息（JSON格式）
     */
    @TableField("BACKUP_INFO")
    private String backupInfo;

    /**
     * 恢复信息（JSON格式）
     */
    @TableField("RESTORE_INFO")
    private String restoreInfo;

    /**
     * 归档时间
     */
    @TableField("ARCHIVE_TIME")
    private LocalDateTime archiveTime;

    /**
     * 归档人ID
     */
    @TableField("ARCHIVED_BY")
    private String archivedBy;

    /**
     * 归档人姓名
     */
    @TableField("ARCHIVED_BY_NAME")
    private String archivedByName;

    /**
     * 是否归档
     */
    @TableField("IS_ARCHIVED")
    private Boolean isArchived;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRY_TIME")
    private LocalDateTime expiryTime;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，archived-已归档
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
    public static final String VERSION_TYPE_INITIAL = "initial";
    public static final String VERSION_TYPE_ADJUSTMENT = "adjustment";
    public static final String VERSION_TYPE_REVISION = "revision";
    public static final String VERSION_TYPE_FINAL = "final";

    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_ARCHIVED = "archived";
}
