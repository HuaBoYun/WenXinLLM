package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_CONCENTRATION_STRATEGY")
@ApiModel(value = "TblConcentrationStrategy", description = "归集策略实体")
public class TblConcentrationStrategy implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "STRATEGY_ID", type = IdType.AUTO)
    @ApiModelProperty("策略ID（自增）")
    private Long strategyId;

    @TableField("STRATEGY_NAME")
    @ApiModelProperty("策略名称")
    private String strategyName;

    @TableField("STRATEGY_CODE")
    @ApiModelProperty("策略编码")
    private String strategyCode;

    @TableField("STRATEGY_TYPE")
    @ApiModelProperty("策略类型")
    private String strategyType;

    @TableField("TARGET_ACCOUNT_ID")
    @ApiModelProperty("目标账户ID")
    private Long targetAccountId;

    @TableField("TARGET_ACCOUNT_NAME")
    @ApiModelProperty("目标账户名称")
    private String targetAccountName;

    @TableField("TARGET_ACCOUNT_NUMBER")
    @ApiModelProperty("目标账户号码")
    private String targetAccountNumber;

    @TableField("CONCENTRATION_CONDITION")
    @ApiModelProperty("归集条件")
    private String concentrationCondition;

    @TableField("FREQUENCY")
    @ApiModelProperty("执行频率")
    private String frequency;

    @TableField("EXECUTION_TIME")
    @ApiModelProperty("执行时间")
    private String executionTime;

    @TableField("PRIORITY")
    @ApiModelProperty("优先级")
    private Integer priority;

    @TableField("STATUS")
    @ApiModelProperty("状态")
    private String status;

    @TableField("SOURCE_ACCOUNT_COUNT")
    @ApiModelProperty("源账户数量")
    private Integer sourceAccountCount;

    @TableField("LAST_EXECUTION_TIME")
    @ApiModelProperty("上次执行时间")
    private Date lastExecutionTime;

    @TableField("NEXT_EXECUTION_TIME")
    @ApiModelProperty("下次执行时间")
    private Date nextExecutionTime;

    @TableField("SUCCESS_RATE")
    @ApiModelProperty("成功率")
    private BigDecimal successRate;

    @TableField("CREATOR_ID")
    @ApiModelProperty("创建人ID")
    private String creatorId;

    @TableField("CREATOR_NAME")
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @TableField("DESCRIPTION")
    @ApiModelProperty("描述")
    private String description;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty("更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    public Long getStrategyId() { return strategyId; }
    public void setStrategyId(Long strategyId) { this.strategyId = strategyId; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public String getStrategyCode() { return strategyCode; }
    public void setStrategyCode(String strategyCode) { this.strategyCode = strategyCode; }
    public String getStrategyType() { return strategyType; }
    public void setStrategyType(String strategyType) { this.strategyType = strategyType; }
    public Long getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(Long targetAccountId) { this.targetAccountId = targetAccountId; }
    public String getTargetAccountName() { return targetAccountName; }
    public void setTargetAccountName(String targetAccountName) { this.targetAccountName = targetAccountName; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }
    public String getConcentrationCondition() { return concentrationCondition; }
    public void setConcentrationCondition(String concentrationCondition) { this.concentrationCondition = concentrationCondition; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getExecutionTime() { return executionTime; }
    public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getSourceAccountCount() { return sourceAccountCount; }
    public void setSourceAccountCount(Integer sourceAccountCount) { this.sourceAccountCount = sourceAccountCount; }
    public Date getLastExecutionTime() { return lastExecutionTime; }
    public void setLastExecutionTime(Date lastExecutionTime) { this.lastExecutionTime = lastExecutionTime; }
    public Date getNextExecutionTime() { return nextExecutionTime; }
    public void setNextExecutionTime(Date nextExecutionTime) { this.nextExecutionTime = nextExecutionTime; }
    public BigDecimal getSuccessRate() { return successRate; }
    public void setSuccessRate(BigDecimal successRate) { this.successRate = successRate; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
