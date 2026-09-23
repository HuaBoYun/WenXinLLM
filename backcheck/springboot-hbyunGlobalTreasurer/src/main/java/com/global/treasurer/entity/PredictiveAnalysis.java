package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预测分析实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PREDICTIVE_ANALYSIS")
public class PredictiveAnalysis implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分析ID
     */
    @TableId(value = "ANALYSIS_ID", type = IdType.AUTO)
    private Long analysisId;

    /**
     * 分析编号
     */
    private String analysisNo;

    /**
     * 分析名称
     */
    private String analysisName;

    /**
     * 分析类型
     */
    private String analysisType;

    /**
     * 预测目标
     */
    private String predictionTarget;

    /**
     * 预测类型(CASH_FLOW/EXCHANGE_RATE/INTEREST_RATE/RISK_METRIC/MARKET_TREND)
     */
    private String predictionType;

    /**
     * 关联模型ID
     */
    private Long modelId;

    /**
     * 输入数据(JSON格式)
     */
    private String inputData;

    /**
     * 预测结果(JSON格式)
     */
    private String predictionResult;

    /**
     * 实际结果(JSON格式)
     */
    private String actualResult;

    /**
     * 置信区间(JSON格式)
     */
    private String confidenceInterval;

    /**
     * 预测周期(DAILY/WEEKLY/MONTHLY/QUARTERLY/YEARLY)
     */
    private String predictionPeriod;

    /**
     * 预测期限(天)
     */
    private Integer predictionHorizon;

    /**
     * 置信水平(0-100)
     */
    private BigDecimal confidenceLevel;

    /**
     * 准确率(百分比)
     */
    private BigDecimal accuracy;

    /**
     * 偏差分析结果
     */
    private String deviationAnalysis;

    /**
     * 分析状态(PENDING/RUNNING/COMPLETED/FAILED)
     */
    private String analysisStatus;

    /**
     * 分析日期
     */
    private LocalDate analysisDate;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

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

    /**
     * 错误信息
     */
    private String errorMessage;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAnalysisId() { return analysisId; }
    public void setAnalysisId(Long analysisId) { this.analysisId = analysisId; }
    public String getAnalysisNo() { return analysisNo; }
    public void setAnalysisNo(String analysisNo) { this.analysisNo = analysisNo; }
    public String getAnalysisName() { return analysisName; }
    public void setAnalysisName(String analysisName) { this.analysisName = analysisName; }
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    public String getPredictionTarget() { return predictionTarget; }
    public void setPredictionTarget(String predictionTarget) { this.predictionTarget = predictionTarget; }
    public String getPredictionType() { return predictionType; }
    public void setPredictionType(String predictionType) { this.predictionType = predictionType; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    public String getPredictionResult() { return predictionResult; }
    public void setPredictionResult(String predictionResult) { this.predictionResult = predictionResult; }
    public String getActualResult() { return actualResult; }
    public void setActualResult(String actualResult) { this.actualResult = actualResult; }
    public String getConfidenceInterval() { return confidenceInterval; }
    public void setConfidenceInterval(String confidenceInterval) { this.confidenceInterval = confidenceInterval; }
    public String getPredictionPeriod() { return predictionPeriod; }
    public void setPredictionPeriod(String predictionPeriod) { this.predictionPeriod = predictionPeriod; }
    public Integer getPredictionHorizon() { return predictionHorizon; }
    public void setPredictionHorizon(Integer predictionHorizon) { this.predictionHorizon = predictionHorizon; }
    public BigDecimal getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(BigDecimal confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    public BigDecimal getAccuracy() { return accuracy; }
    public void setAccuracy(BigDecimal accuracy) { this.accuracy = accuracy; }
    public String getDeviationAnalysis() { return deviationAnalysis; }
    public void setDeviationAnalysis(String deviationAnalysis) { this.deviationAnalysis = deviationAnalysis; }
    public String getAnalysisStatus() { return analysisStatus; }
    public void setAnalysisStatus(String analysisStatus) { this.analysisStatus = analysisStatus; }
    public LocalDate getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(LocalDate analysisDate) { this.analysisDate = analysisDate; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
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
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
