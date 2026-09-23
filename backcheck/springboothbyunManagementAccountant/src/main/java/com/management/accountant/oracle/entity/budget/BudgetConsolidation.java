package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
 import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算合并实体
 */
@Data
 @JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_CONSOLIDATION")
public class BudgetConsolidation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CONSOLIDATION_ID", type = IdType.ASSIGN_UUID)
    private String consolidationId;

    @TableField("CONSOLIDATION_CODE")
    private String consolidationCode;

    @TableField("CONSOLIDATION_NAME")
    private String consolidationName;

    @TableField("CONSOLIDATION_TYPE")
    private String consolidationType;

    /** 合并状态 DRAFT/EXECUTING/COMPLETED/FAILED */
    @TableField("CONSOLIDATION_STATUS")
    private String consolidationStatus;

    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /** 合并总金额 */
    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    /** 合并规则（JSON） */
    @TableField("CONSOLIDATION_RULES")
    private String consolidationRules;

    /** 执行时间 */
    @TableField("EXECUTE_TIME")
    private Date executeTime;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private Integer delFlag;

    @TableField("TENANT_ID")
    private String tenantId;

    // ---- 以下字段在实际表中不存在，忽略映射 ----
    @TableField(exist = false)
    private String consolidationScope;

    @TableField(exist = false)
    private String consolidationRule;

    @TableField(exist = false)
    private BigDecimal consolidationAmount;

    @TableField(exist = false)
    private Date consolidationDate;

    @TableField(exist = false)
    private String consolidationDescription;
}


