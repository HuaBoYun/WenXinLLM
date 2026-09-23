package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算限额实体
 * 对应表: TBL_BUDGET_LIMIT
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_LIMIT")
public class BudgetLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 限额ID
     */
    @TableId(value = "LIMIT_ID", type = IdType.ASSIGN_UUID)
    private String limitId;

    /**
     * 限额编码
     */
    @TableField("LIMIT_CODE")
    @ExcelField(title = "限额编码", sort = 10, width = 4000)
    private String limitCode;

    /**
     * 限额名称
     */
    @TableField("LIMIT_NAME")
    @ExcelField(title = "限额名称", sort = 20, width = 6000)
    private String limitName;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 限额类型 (DAILY/MONTHLY/QUARTERLY/YEARLY)
     */
    @TableField("LIMIT_TYPE")
    @ExcelField(title = "限额类型", sort = 30, width = 3000)
    private String limitType;

    /**
     * 限额金额
     */
    @TableField("LIMIT_AMOUNT")
    @ExcelField(title = "限额金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal limitAmount;

    /**
     * 已用金额
     */
    @TableField("USED_AMOUNT")
    @ExcelField(title = "已用金额", sort = 50, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal usedAmount;

    /**
     * 冻结金额
     */
    @TableField("FROZEN_AMOUNT")
    @ExcelField(title = "冻结金额", sort = 55, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal frozenAmount;

    /**
     * 可用金额
     */
    @TableField("AVAILABLE_AMOUNT")
    @ExcelField(title = "可用金额", sort = 60, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal availableAmount;

    /**
     * 组织单元ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    @ExcelField(title = "生效日期", sort = 80, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRE_DATE")
    @ExcelField(title = "失效日期", sort = 90, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 状态 (NORMAL/EXCEEDED/FROZEN)
     */
    @TableField("STATUS")
    @ExcelField(title = "状态", sort = 100, width = 3000)
    private String status;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    @ExcelField(title = "描述", sort = 120, width = 8000)
    private String description;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(title = "创建时间", sort = 130, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;
}

