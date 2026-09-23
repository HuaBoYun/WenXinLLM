package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 核销记录实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_WRITE_OFF_RECORD")
public class TblWriteOffRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 核销ID */
    @TableId(value = "WRITE_OFF_ID", type = IdType.ASSIGN_UUID)
    private String writeOffId;

    /** 核销单号 */
    @TableField("WRITE_OFF_NO")
    private String writeOffNo;

    /** 付款ID */
    @TableField("PAYMENT_ID")
    private String paymentId;

    /** 应付单据ID */
    @TableField("DOCUMENT_ID")
    private String documentId;

    /** 核销金额 */
    @TableField("WRITE_OFF_AMOUNT")
    private BigDecimal writeOffAmount;

    /** 核销日期 */
    @TableField("WRITE_OFF_DATE")
    private LocalDate writeOffDate;

    /** 核销状态(0:待核销,1:已核销,2:已撤销) */
    @TableField("WRITE_OFF_STATUS")
    private Integer writeOffStatus;

    /** 摘要 */
    @TableField("SUMMARY")
    private String summary;

    /** 备注 */
    @TableField("REMARKS")
    private String remarks;

    /** 创建时间 */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 创建人 */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /** 租户ID */
    @TableField("TENANT_ID")
    private String tenantId;

    /** 逻辑删除标记 */
    @TableLogic
    @TableField("IS_DELETED")
    private Integer isDeleted;
}

