package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_DATA_MINING_TASK")
public class DataMiningTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String taskId;
    private String taskName;
    private String miningType;
    private String algorithm;
    private String dataSource;
    private Double minSupport;
    private Double minConfidence;
    private String featureSelection;
    private String algorithmParameters;
    private String taskStatus;
    private Integer patternsFound;   // 发现模式数量
    private Double accuracyRate;     // 挖掘准确率(0-100)
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer delFlag;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getMiningType() { return miningType; }
    public void setMiningType(String miningType) { this.miningType = miningType; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public Double getMinSupport() { return minSupport; }
    public void setMinSupport(Double minSupport) { this.minSupport = minSupport; }

    public Double getMinConfidence() { return minConfidence; }
    public void setMinConfidence(Double minConfidence) { this.minConfidence = minConfidence; }

    public String getFeatureSelection() { return featureSelection; }
    public void setFeatureSelection(String featureSelection) { this.featureSelection = featureSelection; }

    public String getAlgorithmParameters() { return algorithmParameters; }
    public void setAlgorithmParameters(String algorithmParameters) { this.algorithmParameters = algorithmParameters; }

    public String getTaskStatus() { return taskStatus; }
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }

    public Integer getPatternsFound() { return patternsFound; }
    public void setPatternsFound(Integer patternsFound) { this.patternsFound = patternsFound; }

    public Double getAccuracyRate() { return accuracyRate; }
    public void setAccuracyRate(Double accuracyRate) { this.accuracyRate = accuracyRate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
}
