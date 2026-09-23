package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会计凭证实体类 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_ACCOUNTING_VOUCHER")
public class AccountingVoucherEntity {

    /**
     * 凭证ID
     */
    @TableId(value = "VOUCHER_ID", type = IdType.INPUT)
    private Long voucherId;

    /**
     * 凭证编号
     */
    @TableField("VOUCHER_NO")
    private String voucherNo;

    /**
     * 凭证类型ID
     */
    @TableField("VOUCHER_TYPE_ID")
    private Long voucherTypeId;

    /**
     * 凭证日期
     */
    @TableField("VOUCHER_DATE")
    private LocalDate voucherDate;

    /**
     * 会计期间
     */
    @TableField("ACCOUNTING_PERIOD")
    private String accountingPeriod;

    /**
     * 凭证摘要
     */
    @TableField("VOUCHER_DESC")
    private String voucherDesc;

    /**
     * 借方金额合计
     */
    @TableField("TOTAL_DEBIT")
    private BigDecimal totalDebit;

    /**
     * 贷方金额合计
     */
    @TableField("TOTAL_CREDIT")
    private BigDecimal totalCredit;

    /**
     * 凭证状态(1草稿2已保存3已过账)
     */
    @TableField("VOUCHER_STATUS")
    private Integer voucherStatus;

    /**
     * 审核状态(0未审核1已审核)
     */
    @TableField("AUDIT_STATUS")
    private Integer auditStatus;

    /**
     * 审核人
     */
    @TableField("AUDITOR")
    private Long auditor;

    /**
     * 审核时间
     */
    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private Integer version;

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
