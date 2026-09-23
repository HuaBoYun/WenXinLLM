package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收入分配表实体类
 */
@Data
@TableName("T_REVENUE_ALLOCATION")
public class RevenueAllocationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分配ID
     */
    @TableId(value = "ALLOCATION_ID", type = IdType.ASSIGN_ID)
    private Long allocationId;

    /**
     * 分配单号
     */
    @TableField("ALLOCATION_NO")
    private String allocationNo;

    /**
     * 分配类型(1部门2产品3项目4客户)
     */
    @TableField("ALLOCATION_TYPE")
    private Integer allocationType;

    /**
     * 分配期间
     */
    @TableField("ALLOCATION_PERIOD")
    private String allocationPeriod;

    /**
     * 总金额
     */
    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    /**
     * 已分配金额
     */
    @TableField("ALLOCATED_AMOUNT")
    private BigDecimal allocatedAmount;

    /**
     * 剩余金额
     */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /**
     * 分配状态(0待分配1已分配)
     */
    @TableField("ALLOCATION_STATUS")
    private Integer allocationStatus;

    /**
     * 分配日期
     */
    @TableField("ALLOCATION_DATE")
    private LocalDate allocationDate;

    /**
     * 分配规则
     */
    @TableField("ALLOCATION_RULE")
    private String allocationRule;

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
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private Long creator;

    /**
     * 更新人
     */
    @TableField("UPDATER")
    private Long updater;
}

