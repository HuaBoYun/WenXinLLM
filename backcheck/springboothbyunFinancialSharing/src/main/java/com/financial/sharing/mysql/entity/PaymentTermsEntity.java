package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账期设置实体类 - Oracle/达梦版本
 * 
 * @author Financial Sharing System
 * @since 2024-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_PAYMENT_TERMS")
public class PaymentTermsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账期ID
     */
    @TableId(value = "TERMS_ID", type = IdType.ASSIGN_ID)
    private Long termsId;

    /**
     * 账期编码
     */
    @TableField("TERMS_CODE")
    private String termsCode;

    /**
     * 账期名称
     */
    @TableField("TERMS_NAME")
    private String termsName;

    /**
     * 供应商ID
     */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /**
     * 付款天数
     */
    @TableField("PAYMENT_DAYS")
    private Integer paymentDays;

    /**
     * 折扣天数
     */
    @TableField("DISCOUNT_DAYS")
    private Integer discountDays;

    /**
     * 折扣率
     */
    @TableField("DISCOUNT_RATE")
    private BigDecimal discountRate;

    /**
     * 状态: 1-启用, 0-停用
     */
    @TableField("STATUS")
    private Integer status;

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

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

