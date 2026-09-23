package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 递延收入实体类 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_DEFERRED_REVENUE")
public class DeferredRevenueEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 递延ID
     */
    @TableId(value = "DEFERRED_ID", type = IdType.ASSIGN_ID)
    private Long deferredId;

    /**
     * 递延单号
     */
    @TableField("DEFERRED_NO")
    private String deferredNo;

    /**
     * 合同ID
     */
    @TableField("CONTRACT_ID")
    private Long contractId;

    /**
     * 递延类型(1预收款2递延收入)
     */
    @TableField("DEFERRED_TYPE")
    private Integer deferredType;

    /**
     * 递延期间
     */
    @TableField("DEFERRED_PERIOD")
    private String deferredPeriod;

    /**
     * 原始金额
     */
    @TableField("ORIGINAL_AMOUNT")
    private BigDecimal originalAmount;

    /**
     * 递延金额
     */
    @TableField("DEFERRED_AMOUNT")
    private BigDecimal deferredAmount;

    /**
     * 已确认金额
     */
    @TableField("RECOGNIZED_AMOUNT")
    private BigDecimal recognizedAmount;

    /**
     * 剩余金额
     */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /**
     * 递延状态(0待确认1确认中2已完成)
     */
    @TableField("DEFERRED_STATUS")
    private Integer deferredStatus;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    private LocalDate endDate;

    /**
     * 确认方法(1按时间2按进度3按事件)
     */
    @TableField("RECOGNITION_METHOD")
    private Integer recognitionMethod;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 更新人
     */
    @TableField(value = "UPDATER", fill = FieldFill.INSERT_UPDATE)
    private Long updater;
}

