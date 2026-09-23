package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
    /** 补充字段（来源: 调用点签名反推） */
    private Long lockedBy;

    /** 补充字段（来源: 调用点签名反推） */
    private String lockedByName;

    /** 补充字段（来源: 调用点签名反推） */
    private String lockedTime;



/**
 * 预算数据表
 * 对应表：tbl_eps_budget_data
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_data")
public class EpsBudgetData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID，主键
     */
    @TableId(value = "data_id", type = IdType.AUTO)
    private Long dataId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 预算版本ID
     */
    @TableField("version_id")
    private Long versionId;

    /**
     * 预算科目ID
     */
    @TableField("subject_id")
    private Long subjectId;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 预算主体ID
     */
    @TableField("entity_id")
    private Long entityId;

    /**
     * 预算期间
     */
    @TableField("budget_period")
    private String budgetPeriod;

    /**
     * 期间开始日期
     */
    @TableField("period_start_date")
    private LocalDate periodStartDate;

    /**
     * 期间结束日期
     */
    @TableField("period_end_date")
    private LocalDate periodEndDate;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 汇率
     */
    @TableField("exchange_rate")
    private BigDecimal exchangeRate;

    /**
     * 原币金额
     */
    @TableField("original_amount")
    private BigDecimal originalAmount;

    /**
     * 本币金额
     */
    @TableField("local_amount")
    private BigDecimal localAmount;
    /** 预算金额 */
    private BigDecimal budgetAmount;
    /** 实际金额 */
    private BigDecimal actualAmount;
    /** 差异金额 */
    private BigDecimal varianceAmount;
    /** 数据状态 */
    private String dataStatus;


    /**
     * 数量
     */
    @TableField("quantity")
    private BigDecimal quantity;

    /**
     * 单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 计量单位
     */
    @TableField("unit_of_measure")
    private String unitOfMeasure;

    /**
     * 维度1值
     */
    @TableField("dimension1_value")
    private String dimension1Value;

    /**
     * 维度2值
     */
    @TableField("dimension2_value")
    private String dimension2Value;

    /**
     * 维度3值
     */
    @TableField("dimension3_value")
    private String dimension3Value;

    /**
     * 维度4值
     */
    @TableField("dimension4_value")
    private String dimension4Value;

    /**
     * 维度5值
     */
    @TableField("dimension5_value")
    private String dimension5Value;

    /**
     * 扩展维度，JSON格式存储
     */
    @TableField("extended_dimensions")
    private String extendedDimensions;

    /**
     * 数据来源：MANUAL-手工录入/SYSTEM-系统计算/IMPORT-导入/COPY-复制
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 来源单据ID
     */
    @TableField("source_document_id")
    private String sourceDocumentId;

    /**
     * 来源单据类型
     */
    @TableField("source_document_type")
    private String sourceDocumentType;

    /**
     * 计算公式
     */
    @TableField("calculation_formula")
    private String calculationFormula;

    /**
     * 计算结果
     */
    @TableField("calculation_result")
    private BigDecimal calculationResult;

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
     * 审批状态：DRAFT-草稿/PENDING-待审批/APPROVED-已审批/REJECTED-已拒绝
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("approval_comment")
    private String approvalComment;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 附件信息，JSON格式存储
     */
    @TableField("attachments")
    private String attachments;

    /**
     * 状态：DRAFT-草稿/ACTIVE-激活/INACTIVE-停用/ARCHIVED-归档
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
