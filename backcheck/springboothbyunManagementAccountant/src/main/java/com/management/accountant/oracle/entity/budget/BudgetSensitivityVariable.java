package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算敏感性变量实体
 *
 * @author AI Agent
 * @date 2026-04-10
 */
@Data
@TableName("TBL_BUDGET_SENSITIVITY_VARIABLE")
public class BudgetSensitivityVariable {

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

    @TableField("MIN_VALUE")
    private BigDecimal minValue;

    @TableField("MAX_VALUE")
    private BigDecimal maxValue;

    @TableField("STEP_SIZE")
    private BigDecimal stepSize;

    /** 优先级 1-5 */
    @TableField("PRIORITY")
    private Integer priority;

    /** 是否启用 1-启用 0-禁用 */
    @TableField("ENABLED")
    private Integer enabled;

    /** 敏感性系数（分析后计算得出） */
    @TableField("SENSITIVITY_COEFFICIENT")
    private BigDecimal sensitivityCoefficient;

    /** HIGH/MEDIUM/LOW/NONE */
    @TableField("SENSITIVITY_LEVEL")
    private String sensitivityLevel;

    @TableField("DESCRIPTION")
    private String description;

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

    // ---- getter/setter ----
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
    public BigDecimal getMinValue() { return minValue; }
    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }
    public BigDecimal getMaxValue() { return maxValue; }
    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }
    public BigDecimal getStepSize() { return stepSize; }
    public void setStepSize(BigDecimal stepSize) { this.stepSize = stepSize; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    public BigDecimal getSensitivityCoefficient() { return sensitivityCoefficient; }
    public void setSensitivityCoefficient(BigDecimal sensitivityCoefficient) { this.sensitivityCoefficient = sensitivityCoefficient; }
    public String getSensitivityLevel() { return sensitivityLevel; }
    public void setSensitivityLevel(String sensitivityLevel) { this.sensitivityLevel = sensitivityLevel; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
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
