package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收入确认实体类 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_REVENUE_RECOGNITION")
public class RevenueRecognitionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 确认ID
     */
    @TableId(value = "RECOGNITION_ID", type = IdType.ASSIGN_ID)
    private Long recognitionId;

    /**
     * 合同ID
     */
    @TableField("CONTRACT_ID")
    private Long contractId;

    /**
     * 确认类型(1自动确认2手动确认)
     */
    @TableField("RECOGNITION_TYPE")
    private Integer recognitionType;

    /**
     * 确认期间
     */
    @TableField("RECOGNITION_PERIOD")
    private String recognitionPeriod;

    /**
     * 确认日期
     */
    @TableField("RECOGNITION_DATE")
    private LocalDate recognitionDate;

    /**
     * 确认金额
     */
    @TableField("RECOGNITION_AMOUNT")
    private BigDecimal recognitionAmount;

    /**
     * 累计确认金额
     */
    @TableField("CUMULATIVE_AMOUNT")
    private BigDecimal cumulativeAmount;

    /**
     * 确认比例
     */
    @TableField("RECOGNITION_RATE")
    private BigDecimal recognitionRate;

    /**
     * 履约进度
     */
    @TableField("PERFORMANCE_PROGRESS")
    private BigDecimal performanceProgress;

    /**
     * 确认依据
     */
    @TableField("RECOGNITION_BASIS")
    private String recognitionBasis;

    /**
     * 凭证ID
     */
    @TableField("VOUCHER_ID")
    private Long voucherId;

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

