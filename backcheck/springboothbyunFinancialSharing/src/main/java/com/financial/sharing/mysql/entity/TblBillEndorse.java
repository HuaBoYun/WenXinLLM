package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 票据背书记录实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BILL_ENDORSE")
public class TblBillEndorse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 背书ID */
    @TableId(value = "ENDORSE_ID", type = IdType.ASSIGN_UUID)
    private String endorseId;

    /** 背书编号 */
    @TableField("ENDORSE_NO")
    private String endorseNo;

    /** 票据ID */
    @TableField("BILL_ID")
    private String billId;

    /** 背书日期 */
    @TableField("ENDORSE_DATE")
    private LocalDate endorseDate;

    /** 背书金额 */
    @TableField("ENDORSE_AMOUNT")
    private BigDecimal endorseAmount;

    /** 背书人 */
    @TableField("ENDORSER_NAME")
    private String endorserName;

    /** 被背书人 */
    @TableField("ENDORSEE_NAME")
    private String endorseeName;

    /** 被背书人开户行 */
    @TableField("ENDORSEE_BANK")
    private String endorseeBank;

    /** 被背书人账号 */
    @TableField("ENDORSEE_ACCOUNT")
    private String endorseeAccount;

    /** 背书类型(1:转让,2:质押) */
    @TableField("ENDORSE_TYPE")
    private Integer endorseType;

    /** 背书说明 */
    @TableField("DESCRIPTION")
    private String description;

    /** 状态(pending/approved/rejected/cancelled) */
    @TableField("STATUS")
    private String status;

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

