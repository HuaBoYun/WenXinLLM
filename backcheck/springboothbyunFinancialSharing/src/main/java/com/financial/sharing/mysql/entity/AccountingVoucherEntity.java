package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会计凭证实体类 - MySQL版本
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
    @TableId(value = "VOUCHER_ID", type = IdType.ASSIGN_ID)
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
     * 分录数量
     */
    @TableField("ENTRY_COUNT")
    private Integer entryCount;

    /**
     * 凭证状态(0草稿1已生成2已过账3已取消)
     */
    @TableField("VOUCHER_STATUS")
    private Integer voucherStatus;

    /**
     * 来源事项ID
     */
    @TableField("SOURCE_TRANSACTION_ID")
    private Long sourceTransactionId;

    /**
     * 来源系统
     */
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    /**
     * 制单人
     */
    @TableField("PREPARER")
    private Long preparer;

    /**
     * 审核人
     */
    @TableField("REVIEWER")
    private Long reviewer;

    /**
     * 审核时间
     */
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;

    /**
     * 过账人
     */
    @TableField("POSTER")
    private Long poster;

    /**
     * 过账时间
     */
    @TableField("POST_TIME")
    private LocalDateTime postTime;

    /**
     * 附件数量
     */
    @TableField("ATTACHMENT_COUNT")
    private Integer attachmentCount;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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
