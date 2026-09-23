package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 凭证反过账记录实体类
 *
 * @author system
 * @since 2026-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_VOUCHER_UNPOST_RECORD")
public class VoucherUnpostRecordEntity {

    /**
     * 记录ID（主键）
     */
    @TableId(value = "RECORD_ID", type = IdType.INPUT)
    private Long recordId;

    /**
     * 凭证ID
     */
    @TableField("VOUCHER_ID")
    private Long voucherId;

    /**
     * 凭证号
     */
    @TableField("VOUCHER_NO")
    private String voucherNo;

    /**
     * 凭证类型：ACCOUNTING-记账凭证, RECEIPT-收款凭证, PAYMENT-付款凭证, TRANSFER-转账凭证
     */
    @TableField("VOUCHER_TYPE")
    private String voucherType;

    /**
     * 凭证金额
     */
    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    /**
     * 原过账时间
     */
    @TableField("POSTING_TIME")
    private LocalDateTime postingTime;

    /**
     * 原过账人ID
     */
    @TableField("POSTING_BY")
    private Long postingBy;

    /**
     * 原过账人姓名
     */
    @TableField("POSTING_BY_NAME")
    private String postingByName;

    /**
     * 反过账原因
     */
    @TableField("UNPOST_REASON")
    private String unpostReason;

    /**
     * 反过账时间
     */
    @TableField("UNPOST_TIME")
    private LocalDateTime unpostTime;

    /**
     * 反过账操作人ID
     */
    @TableField("UNPOST_BY")
    private Long unpostBy;

    /**
     * 反过账操作人姓名
     */
    @TableField("UNPOST_BY_NAME")
    private String unpostByName;

    /**
     * 状态：UNPOSTED-已撤销, REPOSTED-已重新过账
     */
    @TableField("UNPOST_STATUS")
    private String unpostStatus;

    /**
     * 重新过账时间
     */
    @TableField("REPOST_TIME")
    private LocalDateTime repostTime;

    /**
     * 重新过账人ID
     */
    @TableField("REPOST_BY")
    private Long repostBy;

    /**
     * 重新过账人姓名
     */
    @TableField("REPOST_BY_NAME")
    private String repostByName;

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
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;
}

