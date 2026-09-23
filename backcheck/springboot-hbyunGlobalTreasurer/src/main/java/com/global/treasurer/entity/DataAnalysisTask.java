package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据分析任务实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_DATA_ANALYSIS_TASK")
public class DataAnalysisTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "TASK_ID", type = IdType.AUTO)
    private Long taskId;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型(DESCRIPTIVE/DIAGNOSTIC/PREDICTIVE/PRESCRIPTIVE)
     */
    private String taskType;

    /**
     * 分析类型
     */
    private String analysisType;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 数据范围
     */
    private String dataRange;

    /**
     * 分析参数(JSON格式)
     */
    private String analysisParameters;

    /**
     * 关联模型ID
     */
    private Long modelId;

    /**
     * 调度类型(MANUAL/SCHEDULED/TRIGGERED)
     */
    private String scheduleType;

    /**
     * 调度表达式(cron表达式)
     */
    private String scheduleCron;

    /**
     * 结果格式(JSON/XML/CSV/EXCEL)
     */
    private String resultFormat;

    /**
     * 任务状态(PENDING/RUNNING/COMPLETED/FAILED/CANCELLED)
     */
    private String taskStatus;

    /**
     * 执行时长(秒)
     */
    private Integer executionTime;

    /**
     * 执行人ID
     */
    private Long executeUser;

    /**
     * 开始时间(非数据库字段)
     */
    @TableField(exist = false)
    private LocalDateTime startTime;

    /**
     * 结束时间(非数据库字段)
     */
    @TableField(exist = false)
    private LocalDateTime endTime;

    /**
     * 执行结果
     */
    private String executionResult;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志(0正常1删除)
     */
    private String delFlag;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getTaskNo() { return taskNo; }
    public void setTaskNo(String taskNo) { this.taskNo = taskNo; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getDataRange() { return dataRange; }
    public void setDataRange(String dataRange) { this.dataRange = dataRange; }
    public String getAnalysisParameters() { return analysisParameters; }
    public void setAnalysisParameters(String analysisParameters) { this.analysisParameters = analysisParameters; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public String getScheduleType() { return scheduleType; }
    public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }
    public String getScheduleCron() { return scheduleCron; }
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    public String getResultFormat() { return resultFormat; }
    public void setResultFormat(String resultFormat) { this.resultFormat = resultFormat; }
    public String getTaskStatus() { return taskStatus; }
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
    public Integer getExecutionTime() { return executionTime; }
    public void setExecutionTime(Integer executionTime) { this.executionTime = executionTime; }
    public Long getExecuteUser() { return executeUser; }
    public void setExecuteUser(Long executeUser) { this.executeUser = executeUser; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getExecutionResult() { return executionResult; }
    public void setExecutionResult(String executionResult) { this.executionResult = executionResult; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

}
