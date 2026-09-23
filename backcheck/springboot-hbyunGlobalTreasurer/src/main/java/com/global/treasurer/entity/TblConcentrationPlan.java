package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 归集计划实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CONCENTRATION_PLAN")
@ApiModel(value = "TblConcentrationPlan", description = "归集计划实体")
public class TblConcentrationPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PLAN_ID", type = IdType.AUTO)
    @ApiModelProperty("计划ID")
    private Long planId;

    @TableField("PLAN_NAME")
    @ApiModelProperty("计划名称")
    private String planName;

    @TableField("STRATEGY_ID")
    @ApiModelProperty("策略ID")
    private Long strategyId;

    @TableField("STRATEGY_NAME")
    @ApiModelProperty("策略名称")
    private String strategyName;

    @TableField("PLAN_AMOUNT")
    @ApiModelProperty("计划金额")
    private BigDecimal planAmount;

    @TableField("ACTUAL_AMOUNT")
    @ApiModelProperty("实际金额")
    private BigDecimal actualAmount;

    @TableField("EXECUTION_PROGRESS")
    @ApiModelProperty("执行进度")
    private Integer executionProgress;

    @TableField("PLAN_STATUS")
    @ApiModelProperty("计划状态(PENDING-待执行/EXECUTING-执行中/PAUSED-已暂停/COMPLETED-已完成/CANCELLED-已取消)")
    private String planStatus;

    @TableField("EXECUTION_TIME")
    @ApiModelProperty("执行时间")
    private Date executionTime;

    @TableField("CREATOR_ID")
    @ApiModelProperty("创建人ID")
    private String creatorId;

    @TableField("CREATOR_NAME")
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @TableField("PLAN_DESCRIPTION")
    @ApiModelProperty("计划描述")
    private String planDescription;

    @TableField("RISK_CONTROLS")
    @ApiModelProperty("风险控制")
    private String riskControls;

    @TableField("NOTIFICATIONS")
    @ApiModelProperty("通知设置")
    private String notifications;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Long getStrategyId() { return strategyId; }
    public void setStrategyId(Long strategyId) { this.strategyId = strategyId; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public BigDecimal getPlanAmount() { return planAmount; }
    public void setPlanAmount(BigDecimal planAmount) { this.planAmount = planAmount; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public Integer getExecutionProgress() { return executionProgress; }
    public void setExecutionProgress(Integer executionProgress) { this.executionProgress = executionProgress; }
    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
    public Date getExecutionTime() { return executionTime; }
    public void setExecutionTime(Date executionTime) { this.executionTime = executionTime; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    public String getPlanDescription() { return planDescription; }
    public void setPlanDescription(String planDescription) { this.planDescription = planDescription; }
    public String getRiskControls() { return riskControls; }
    public void setRiskControls(String riskControls) { this.riskControls = riskControls; }
    public String getNotifications() { return notifications; }
    public void setNotifications(String notifications) { this.notifications = notifications; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
