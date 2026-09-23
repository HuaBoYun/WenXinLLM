package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账龄分析快照实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_AGING_SNAPSHOT")
@ApiModel(value = "ArAgingSnapshotEntity对象", description = "账龄分析快照表")
public class ArAgingSnapshotEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快照ID")
    @TableId(value = "SNAPSHOT_ID", type = IdType.ASSIGN_UUID)
    private String snapshotId;

    @ApiModelProperty(value = "分析日期")
    @TableField("ANALYSIS_DATE")
    private LocalDate analysisDate;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "应收单ID")
    @TableField("RECEIVABLE_ID")
    private String receivableId;

    @ApiModelProperty(value = "应收金额")
    @TableField("RECEIVABLE_AMOUNT")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "剩余金额")
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    @ApiModelProperty(value = "账龄天数")
    @TableField("AGING_DAYS")
    private Integer agingDays;

    @ApiModelProperty(value = "账龄区间(0-30/31-60/61-90/91-180/180+)")
    @TableField("AGING_RANGE")
    private String agingRange;

    @ApiModelProperty(value = "风险等级(1低 2中 3高 4极高)")
    @TableField("RISK_LEVEL")
    private Integer riskLevel;

    @ApiModelProperty(value = "风险评分")
    @TableField("RISK_SCORE")
    private Integer riskScore;

    @ApiModelProperty(value = "逾期金额")
    @TableField("OVERDUE_AMOUNT")
    private BigDecimal overdueAmount;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // ========== 扩展字段（不映射到数据库）==========

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "应收单号")
    @TableField(exist = false)
    private String documentNo;

    @ApiModelProperty(value = "风险等级名称")
    @TableField(exist = false)
    private String riskLevelName;

    @ApiModelProperty(value = "到期日期")
    @TableField(exist = false)
    private LocalDate dueDate;

    @ApiModelProperty(value = "业务类型名称")
    @TableField(exist = false)
    private String businessTypeName;
}

