package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 凭证分录实体类 - MySQL版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_VOUCHER_ENTRY")
public class VoucherEntryEntity {

    /**
     * 分录ID
     */
    @TableId(value = "ENTRY_ID", type = IdType.ASSIGN_ID)
    private Long entryId;

    /**
     * 凭证ID
     */
    @TableField("VOUCHER_ID")
    private Long voucherId;

    /**
     * 分录序号
     */
    @TableField("ENTRY_SEQ")
    private Integer entrySeq;

    /**
     * 科目ID
     */
    @TableField("SUBJECT_ID")
    private Long subjectId;

    /**
     * 借方金额
     */
    @TableField("DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    @TableField("CREDIT_AMOUNT")
    private BigDecimal creditAmount;

    /**
     * 币种编码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 汇率
     */
    @TableField("EXCHANGE_RATE")
    private BigDecimal exchangeRate;

    /**
     * 原币借方金额
     */
    @TableField("ORIGINAL_DEBIT")
    private BigDecimal originalDebit;

    /**
     * 原币贷方金额
     */
    @TableField("ORIGINAL_CREDIT")
    private BigDecimal originalCredit;

    /**
     * 辅助核算信息(JSON格式)
     */
    @TableField("AUXILIARY_INFO")
    private String auxiliaryInfo;

    /**
     * 分录摘要
     */
    @TableField("ENTRY_DESC")
    private String entryDesc;

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
