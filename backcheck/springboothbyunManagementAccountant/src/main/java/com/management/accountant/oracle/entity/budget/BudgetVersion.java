package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算版本实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_VERSION")
public class BudgetVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本ID (主键)
     */
    @TableId(value = "VERSION_ID", type = IdType.ASSIGN_UUID)
    private String versionId;

    /**
     * 版本名称
     */
    @TableField("VERSION_NAME")
    private String versionName;

    /**
     * 版本号（唯一标识，如 v1.0、v2.0）
     */
    @TableField("VERSION_NUMBER")
    private String versionNumber;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 预算名称
     */
    @TableField("BUDGET_NAME")
    private String budgetName;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private java.math.BigDecimal budgetAmount;

    /**
     * 版本状态 (DRAFT/ACTIVE/ARCHIVED/LOCKED)
     */
    @TableField("VERSION_STATUS")
    private String versionStatus;

    /**
     * 版本类型 (INITIAL/ADJUSTMENT/FORECAST/FINAL)
     */
    @TableField("VERSION_TYPE")
    private String versionType;

    /**
     * 基础版本ID（父版本）
     */
    @TableField("BASE_VERSION_ID")
    private String baseVersionId;

    /**
     * 变更说明（详细，CLOB）
     */
    @TableField("CHANGE_DESCRIPTION")
    private String changeDescription;

    /**
     * 变更摘要
     */
    @TableField("CHANGE_SUMMARY")
    private String changeSummary;

    /**
     * 是否当前版本
     */
    @TableField("IS_CURRENT")
    private Boolean isCurrent;

    /**
     * 是否已发布
     */
    @TableField("IS_PUBLISHED")
    private Boolean isPublished;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private Date expiryDate;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 版本编码（唯一标识）
     */
    @TableField("VERSION_CODE")
    private String versionCode;

    /**
     * 财年
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 版本描述
     */
    @TableField("VERSION_DESCRIPTION")
    private String versionDescription;

    /**
     * 是否激活
     */
    @TableField("IS_ACTIVE")
    private Boolean isActive;

    /**
     * 删除标志 (0未删除 1已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;
}

