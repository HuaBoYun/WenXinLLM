package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 决策模型实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_DECISION_MODEL")
public class DecisionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模型ID
     */
    @TableId(value = "MODEL_ID", type = IdType.AUTO)
    private Long modelId;

    /**
     * 模型编号
     */
    private String modelCode;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型类型(RISK_ASSESSMENT/INVESTMENT_DECISION/FINANCING_DECISION/CASHFLOW_FORECAST)
     */
    private String modelType;

    /**
     * 算法类型(LINEAR_REGRESSION/LOGISTIC_REGRESSION/DECISION_TREE/RANDOM_FOREST/NEURAL_NETWORK)
     */
    private String algorithmType;

    /**
     * 模型状态(ACTIVE/INACTIVE/TESTING)
     */
    private String modelStatus;

    /**
     * 准确率
     */
    private BigDecimal accuracy;

    /**
     * 最后训练时间
     */
    private LocalDateTime lastTrainTime;

    /**
     * 模型描述
     */
    private String modelDescription;

    /**
     * 输入参数(JSON格式)
     */
    private String inputParameters;

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


    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getModelType() { return modelType; }
    public void setModelType(String modelType) { this.modelType = modelType; }
    public String getAlgorithmType() { return algorithmType; }
    public void setAlgorithmType(String algorithmType) { this.algorithmType = algorithmType; }
    public String getModelStatus() { return modelStatus; }
    public void setModelStatus(String modelStatus) { this.modelStatus = modelStatus; }
    public BigDecimal getAccuracy() { return accuracy; }
    public void setAccuracy(BigDecimal accuracy) { this.accuracy = accuracy; }
    public LocalDateTime getLastTrainTime() { return lastTrainTime; }
    public void setLastTrainTime(LocalDateTime lastTrainTime) { this.lastTrainTime = lastTrainTime; }
    public String getModelDescription() { return modelDescription; }
    public void setModelDescription(String modelDescription) { this.modelDescription = modelDescription; }
    public String getInputParameters() { return inputParameters; }
    public void setInputParameters(String inputParameters) { this.inputParameters = inputParameters; }
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
