package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 现金流预算实体类
 * 
 * @description 现金流预算管理实体，支持现金流预算的编制和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_CASH_FLOW")
public class BudgetCashFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 现金流预算编码
     */
    @TableField("CASH_FLOW_CODE")
    private String cashFlowCode;

    /**
     * 现金流预算名称
     */
    @TableField("CASH_FLOW_NAME")
    private String cashFlowName;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 预算期间
     */
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 现金流类型：OPERATING-经营活动，INVESTING-投资活动，FINANCING-筹资活动
     */
    @TableField("CASH_FLOW_TYPE")
    private String cashFlowType;

    /**
     * 现金流方向：INFLOW-现金流入，OUTFLOW-现金流出
     */
    @TableField("CASH_FLOW_DIRECTION")
    private String cashFlowDirection;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 预算状态：DRAFT-草稿，SUBMITTED-已提交，APPROVED-已批准
     */
    @TableField("BUDGET_STATUS")
    private String budgetStatus;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // ==================== 常量定义 ====================

    /**
     * 现金流类型常量
     */
    public static final String CASH_FLOW_TYPE_OPERATING = "OPERATING";
    public static final String CASH_FLOW_TYPE_INVESTING = "INVESTING";
    public static final String CASH_FLOW_TYPE_FINANCING = "FINANCING";

    /**
     * 现金流方向常量
     */
    public static final String CASH_FLOW_DIRECTION_INFLOW = "INFLOW";
    public static final String CASH_FLOW_DIRECTION_OUTFLOW = "OUTFLOW";

    /**
     * 预算状态常量
     */
    public static final String BUDGET_STATUS_DRAFT = "DRAFT";
    public static final String BUDGET_STATUS_SUBMITTED = "SUBMITTED";
    public static final String BUDGET_STATUS_APPROVED = "APPROVED";
}
