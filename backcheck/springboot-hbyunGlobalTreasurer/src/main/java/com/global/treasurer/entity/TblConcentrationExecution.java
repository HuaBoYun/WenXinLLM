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
 * 归集执行记录实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CONCENTRATION_EXECUTION")
@ApiModel(value = "TblConcentrationExecution", description = "归集执行记录实体")
public class TblConcentrationExecution implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "EXECUTION_ID", type = IdType.AUTO)
    @ApiModelProperty("执行ID")
    private Long executionId;

    @TableField("TASK_ID")
    @ApiModelProperty("任务ID")
    private String taskId;

    @TableField("TASK_NAME")
    @ApiModelProperty("任务名称")
    private String taskName;

    @TableField("PLAN_ID")
    @ApiModelProperty("计划ID")
    private Long planId;

    @TableField("STRATEGY_ID")
    @ApiModelProperty("策略ID")
    private Long strategyId;

    @TableField("STRATEGY_NAME")
    @ApiModelProperty("策略名称")
    private String strategyName;

    @TableField("EXECUTION_PROGRESS")
    @ApiModelProperty("执行进度")
    private Integer executionProgress;

    @TableField("EXECUTION_STATUS")
    @ApiModelProperty("执行状态")
    private String executionStatus;

    @TableField("PRIORITY")
    @ApiModelProperty("优先级")
    private String priority;

    @TableField("TOTAL_AMOUNT")
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @TableField("COMPLETED_AMOUNT")
    @ApiModelProperty("已完成金额")
    private BigDecimal completedAmount;

    @TableField("START_TIME")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField("ESTIMATED_END_TIME")
    @ApiModelProperty("预计结束时间")
    private Date estimatedEndTime;

    @TableField("ACTUAL_END_TIME")
    @ApiModelProperty("实际结束时间")
    private Date actualEndTime;

    @TableField("AVG_PROCESS_TIME")
    @ApiModelProperty("平均处理时间")
    private BigDecimal avgProcessTime;

    @TableField("PROCESS_SPEED")
    @ApiModelProperty("处理速度")
    private Integer processSpeed;

    @TableField("SUCCESS_RATE")
    @ApiModelProperty("成功率")
    private BigDecimal successRate;

    @TableField("ERROR_COUNT")
    @ApiModelProperty("错误次数")
    private Integer errorCount;

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


    public Long getExecutionId() { return executionId; }
    public void setExecutionId(Long executionId) { this.executionId = executionId; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getStrategyId() { return strategyId; }
    public void setStrategyId(Long strategyId) { this.strategyId = strategyId; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public Integer getExecutionProgress() { return executionProgress; }
    public void setExecutionProgress(Integer executionProgress) { this.executionProgress = executionProgress; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getCompletedAmount() { return completedAmount; }
    public void setCompletedAmount(BigDecimal completedAmount) { this.completedAmount = completedAmount; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEstimatedEndTime() { return estimatedEndTime; }
    public void setEstimatedEndTime(Date estimatedEndTime) { this.estimatedEndTime = estimatedEndTime; }
    public Date getActualEndTime() { return actualEndTime; }
    public void setActualEndTime(Date actualEndTime) { this.actualEndTime = actualEndTime; }
    public BigDecimal getAvgProcessTime() { return avgProcessTime; }
    public void setAvgProcessTime(BigDecimal avgProcessTime) { this.avgProcessTime = avgProcessTime; }
    public Integer getProcessSpeed() { return processSpeed; }
    public void setProcessSpeed(Integer processSpeed) { this.processSpeed = processSpeed; }
    public BigDecimal getSuccessRate() { return successRate; }
    public void setSuccessRate(BigDecimal successRate) { this.successRate = successRate; }
    public Integer getErrorCount() { return errorCount; }
    public void setErrorCount(Integer errorCount) { this.errorCount = errorCount; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
