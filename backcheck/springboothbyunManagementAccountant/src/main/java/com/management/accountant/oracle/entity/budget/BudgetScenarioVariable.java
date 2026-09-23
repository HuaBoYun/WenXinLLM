package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算场景变量实体
 *
 * @author AI Agent
 * @date 2026-04-10
 */
@Data
@TableName("TBL_BUDGET_SCENARIO_VARIABLE")
public class BudgetScenarioVariable {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("VARIABLE_CODE")
    private String variableCode;

    @TableField("VARIABLE_NAME")
    private String variableName;

    /** REVENUE/COST/MARKET_SHARE/INFLATION_RATE/EXCHANGE_RATE/INTEREST_RATE/CUSTOM */
    @TableField("VARIABLE_TYPE")
    private String variableType;

    @TableField("BASE_VALUE")
    private BigDecimal baseValue;

    @TableField("OPTIMISTIC_VALUE")
    private BigDecimal optimisticValue;

    @TableField("PESSIMISTIC_VALUE")
    private BigDecimal pessimisticValue;

    /** NORMAL/UNIFORM/TRIANGULAR/BETA */
    @TableField("DISTRIBUTION")
    private String distribution;

    @TableField("CORRELATION")
    private BigDecimal correlation;

    @TableField("DESCRIPTION")
    private String description;

    /** 关联场景ID */
    @TableField("SCENARIO_ID")
    private String scenarioId;

    /** ACTIVE/INACTIVE */
    @TableField("ANALYSIS_STATUS")
    private String analysisStatus;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    // ---- 显式 getter/setter（防止Lombok处理失败）----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVariableCode() { return variableCode; }
    public void setVariableCode(String variableCode) { this.variableCode = variableCode; }

    public String getVariableName() { return variableName; }
    public void setVariableName(String variableName) { this.variableName = variableName; }

    public String getVariableType() { return variableType; }
    public void setVariableType(String variableType) { this.variableType = variableType; }

    public BigDecimal getBaseValue() { return baseValue; }
    public void setBaseValue(BigDecimal baseValue) { this.baseValue = baseValue; }

    public BigDecimal getOptimisticValue() { return optimisticValue; }
    public void setOptimisticValue(BigDecimal optimisticValue) { this.optimisticValue = optimisticValue; }

    public BigDecimal getPessimisticValue() { return pessimisticValue; }
    public void setPessimisticValue(BigDecimal pessimisticValue) { this.pessimisticValue = pessimisticValue; }

    public String getDistribution() { return distribution; }
    public void setDistribution(String distribution) { this.distribution = distribution; }

    public BigDecimal getCorrelation() { return correlation; }
    public void setCorrelation(BigDecimal correlation) { this.correlation = correlation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }

    public String getAnalysisStatus() { return analysisStatus; }
    public void setAnalysisStatus(String analysisStatus) { this.analysisStatus = analysisStatus; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
