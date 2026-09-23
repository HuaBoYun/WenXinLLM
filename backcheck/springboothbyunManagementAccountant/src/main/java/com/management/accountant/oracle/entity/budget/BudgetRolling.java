package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 滚动预算实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_ROLLING")
public class BudgetRolling implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 滚动预算ID
     */
    @TableId(value = "ROLLING_ID", type = IdType.ASSIGN_UUID)
    private String rollingId;

    @TableField("ROLLING_NAME")
    private String rollingName;

    @TableField("BUDGET_ID")
    private String budgetId;

    @TableField("ROLLING_PERIOD")
    private String rollingPeriod;

    @TableField("ROLLING_WINDOW")
    private Integer rollingWindow;

    @TableField("BASE_PERIOD_TYPE")
    private String basePeriodType;

    @TableField("ROLLING_COUNT")
    private Integer rollingCount;

    @TableField("CURRENT_ROLLING_NUMBER")
    private Integer currentRollingNumber;

    @TableField("START_DATE")
    private Date startDate;

    @TableField("END_DATE")
    private Date endDate;

    @TableField("ROLLING_METHOD")
    private String rollingMethod;

    @TableField("ADJUSTMENT_FACTOR")
    private Double adjustmentFactor;

    @TableField("CALCULATION_FORMULA")
    private String calculationFormula;

    @TableField("ROLLING_STATUS")
    private String rollingStatus;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("REMARK")
    private String remark;

    @TableField("ROLLING_TYPE")
    private String rollingType;

    @TableField("ROLLING_CYCLE")
    private String rollingCycle;

    @TableField("ROLLING_CODE")
    private String rollingCode;

    @TableField("LAST_ROLLING_DATE")
    private Date lastRollingDate;

    @TableField("DEL_FLAG")
    private Integer delFlag;

}
