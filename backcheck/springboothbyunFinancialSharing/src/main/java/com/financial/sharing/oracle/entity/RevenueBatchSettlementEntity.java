package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收入批量结账实体类
 * 
 * @author AI Agent
 * @since 2025-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_REVENUE_BATCH_SETTLEMENT")
public class RevenueBatchSettlementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结账ID
     */
    @TableId(value = "SETTLEMENT_ID", type = IdType.ASSIGN_ID)
    private Long settlementId;

    /**
     * 组织编码
     */
    @TableField("ORG_CODE")
    private String orgCode;

    /**
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 会计期间方案
     */
    @TableField("PERIOD_SCHEME")
    private String periodScheme;

    /**
     * 会计期间
     */
    @TableField("ACCOUNTING_PERIOD")
    private String accountingPeriod;

    /**
     * 结账状态(0未结账1已结账)
     */
    @TableField("SETTLEMENT_STATUS")
    private Integer settlementStatus;

    /**
     * 结账人ID
     */
    @TableField("SETTLEMENT_USER_ID")
    private Long settlementUserId;

    /**
     * 结账人姓名
     */
    @TableField("SETTLEMENT_USER_NAME")
    private String settlementUserName;

    /**
     * 结账时间
     */
    @TableField("SETTLEMENT_TIME")
    private LocalDateTime settlementTime;

    /**
     * 取消结账人ID
     */
    @TableField("CANCEL_USER_ID")
    private Long cancelUserId;

    /**
     * 取消结账人姓名
     */
    @TableField("CANCEL_USER_NAME")
    private String cancelUserName;

    /**
     * 取消结账时间
     */
    @TableField("CANCEL_TIME")
    private LocalDateTime cancelTime;

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
}

