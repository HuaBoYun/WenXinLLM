package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账期设置实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PAYMENT_TERMS")
public class TblPaymentTerms implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 账期ID */
    @TableId(value = "TERMS_ID", type = IdType.ASSIGN_UUID)
    private String termsId;

    /** 账期编码 */
    @TableField("TERMS_CODE")
    private String termsCode;

    /** 账期名称 */
    @TableField("TERMS_NAME")
    private String termsName;

    /** 供应商ID(为空表示通用账期) */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /** 付款天数 */
    @TableField("PAYMENT_DAYS")
    private Integer paymentDays;

    /** 折扣天数 */
    @TableField("DISCOUNT_DAYS")
    private Integer discountDays;

    /** 折扣率(0-1) */
    @TableField("DISCOUNT_RATE")
    private BigDecimal discountRate;

    /** 状态(0:停用,1:启用) */
    @TableField("STATUS")
    private Integer status;

    /** 描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 创建时间 */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人 */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /** 更新人 */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 租户ID */
    @TableField("TENANT_ID")
    private String tenantId;

    /** 逻辑删除标记 */
    @TableLogic
    @TableField("IS_DELETED")
    private Integer isDeleted;

    // ========== 非持久化字段 ==========

    /** 供应商名称 */
    @TableField(exist = false)
    private String supplierName;
}

