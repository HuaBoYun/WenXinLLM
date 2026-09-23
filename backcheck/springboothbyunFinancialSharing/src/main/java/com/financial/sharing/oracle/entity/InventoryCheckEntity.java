package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 存货盘点表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_CHECK")
public class InventoryCheckEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点ID
     */
    @TableId(value = "CHECK_ID", type = IdType.ASSIGN_ID)
    private Long checkId;

    /**
     * 盘点单号
     */
    @TableField("CHECK_NO")
    private String checkNo;

    /**
     * 盘点名称
     */
    @TableField("CHECK_NAME")
    private String checkName;

    /**
     * 盘点类型(FULL-全盘,PARTIAL-抽盘,CYCLE-循环盘点,DYNAMIC-动态盘点)
     */
    @TableField("CHECK_TYPE")
    private String checkType;

    /**
     * 盘点类型名称
     */
    @TableField("CHECK_TYPE_NAME")
    private String checkTypeName;

    /**
     * 仓库ID
     */
    @TableField("WAREHOUSE_ID")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @TableField("WAREHOUSE_NAME")
    private String warehouseName;

    /**
     * 存货数量
     */
    @TableField("INVENTORY_COUNT")
    private Integer inventoryCount;

    /**
     * 盘点进度(百分比)
     */
    @TableField("CHECK_PROGRESS")
    private Integer checkProgress;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 盘点状态(0-计划中,1-盘点中,2-已完成,3-已审批,4-已取消)
     */
    @TableField("CHECK_STATUS")
    private Integer checkStatus;

    /**
     * 盘点日期
     */
    @TableField("CHECK_DATE")
    private LocalDate checkDate;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("APPROVE_TIME")
    private LocalDateTime approveTime;

    /**
     * 审批备注
     */
    @TableField("APPROVE_REMARK")
    private String approveRemark;

    /**
     * 凭证数量
     */
    @TableField("VOUCHER_COUNT")
    private Integer voucherCount;

    /**
     * 凭证ID列表(逗号分隔)
     */
    @TableField("VOUCHER_IDS")
    private String voucherIds;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATOR_ID", fill = FieldFill.INSERT)
    private String creatorId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATER_ID", fill = FieldFill.INSERT_UPDATE)
    private String updaterId;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标识(0-未删除,1-已删除)
     */
    @TableField("DELETED")
    @TableLogic
    private Integer deleted;
}

