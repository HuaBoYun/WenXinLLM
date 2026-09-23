package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预算版本表
 * 对应表：tbl_eps_budget_version
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_version")
public class EpsBudgetVersion implements Serializable {
    /** 补充字段（来源: 调用点签名反推） */
    private String versionStatus;

    /** 补充字段（来源: 调用点签名反推） */
    private boolean isDefault;

    /** 补充字段（来源: 调用点签名反推） */
    private boolean isPublished;

    /** 补充字段（来源: 调用点签名反推） */
    private LocalDateTime publishedTime;

    /** 补充字段（来源: 调用点签名反推） */
    private Long publishedBy;

    /** 补充字段（来源: 调用点签名反推） */
    private String publishedByName;

    /** 补充字段（来源: 调用点签名反推） */
    private LocalDateTime lockedTime;

    /** 补充字段（来源: 调用点签名反推） */
    private Long lockedBy;

    /** 补充字段（来源: 调用点签名反推） */
    private String lockedByName;

    /** 补充字段（来源: 调用点签名反推） */
    private boolean isBaseline;

    /** 补充字段（来源: 调用点签名反推） */
    private String parentVersionName;

    /** 补充字段（来源: 调用点签名反推） */
    private boolean isTemplate;



    private static final long serialVersionUID = 1L;

    /**
     * 版本ID，主键
     */
    @TableId(value = "version_id", type = IdType.AUTO)
    private Long versionId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 版本编码，唯一标识
     */
    @TableField("version_code")
    private String versionCode;

    /**
     * 版本名称
     */
    @TableField("version_name")
    private String versionName;

    /**
     * 版本描述
     */
    @TableField("version_description")
    private String versionDescription;

    /**
     * 版本类型：ORIGINAL-原始版本/ADJUSTMENT-调整版本/EXECUTION-执行版本/FORECAST-预测版本
     */
    @TableField("version_type")
    private String versionType;

    /**
     * 父版本ID，用于版本继承
     */
    @TableField("parent_version_id")
    private Long parentVersionId;

    /**
     * 预算期间，如2024年度、2024Q1等
     */
    @TableField("fiscal_period")
    private String fiscalPeriod;

    /**
     * 开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 审批状态：DRAFT-草稿/PENDING-待审批/APPROVED-已审批/REJECTED-已拒绝
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 是否当前版本：0-否/1-是
     */
    @TableField("is_current")
    private Integer isCurrent;

    /**
     * 是否锁定：0-否/1-是
     */
    @TableField("is_locked")
    private Integer isLocked;

    /**
     * 锁定原因
     */
    @TableField("lock_reason")
    private String lockReason;

    /**
     * 是否基线版本：0-否/1-是
     */
    @TableField("baseline_version")
    private Integer baselineVersion;

    /**
     * 变更说明
     */
    @TableField("change_description")
    private String changeDescription;

    /**
     * 版本状态：DRAFT-草稿/ACTIVE-激活/INACTIVE-停用/ARCHIVED-归档
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
