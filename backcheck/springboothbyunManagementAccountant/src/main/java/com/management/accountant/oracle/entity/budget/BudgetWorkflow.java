package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算工作流实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_WORKFLOW")
public class BudgetWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工作流ID (主键)
     */
    @TableId(value = "WORKFLOW_ID", type = IdType.ASSIGN_UUID)
    private String workflowId;

    /**
     * 工作流编码
     */
    @TableField("WORKFLOW_CODE")
    private String workflowCode;

    /**
     * 工作流名称
     */
    @TableField("WORKFLOW_NAME")
    private String workflowName;

    /**
     * 工作流类型
     * APPROVAL: 审批流程
     * NOTIFICATION: 通知流程
     * AUTOMATION: 自动化流程
     */
    @TableField("WORKFLOW_TYPE")
    private String workflowType;

    /**
     * 流程定义 (JSON格式)
     */
    @TableField("WORKFLOW_DEFINITION")
    private String workflowDefinition;

    /**
     * 节点配置 (JSON格式)
     */
    @TableField("NODE_CONFIG")
    private String nodeConfig;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 工作流描述
     */
    @TableField("WORKFLOW_DESCRIPTION")
    private String workflowDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private Integer version;

    /**
     * 工作流状态 (DRAFT/PUBLISHED/ACTIVE/SUSPENDED/ARCHIVED)
     */
    @TableField("WORKFLOW_STATUS")
    private String workflowStatus;

    /**
     * 分类ID
     */
    @TableField("CATEGORY_ID")
    private String categoryId;

    /**
     * 是否允许并行 (0否1是)
     */
    @TableField("ALLOW_PARALLEL")
    private Integer allowParallel;

    /**
     * 是否允许跳过 (0否1是)
     */
    @TableField("ALLOW_SKIP")
    private Integer allowSkip;

    /**
     * 是否自动启动 (0否1是)
     */
    @TableField("AUTO_START")
    private Integer autoStart;

    /**
     * 启动时通知 (0否1是)
     */
    @TableField("NOTIFY_ON_START")
    private Integer notifyOnStart;

    /**
     * 完成时通知 (0否1是)
     */
    @TableField("NOTIFY_ON_COMPLETE")
    private Integer notifyOnComplete;

    /**
     * 实例数量
     */
    @TableField("INSTANCE_COUNT")
    private Integer instanceCount;

    /**
     * 平均耗时(分钟)
     */
    @TableField("AVG_DURATION")
    private Integer avgDuration;

    /**
     * 成功率(百分比)
     */
    @TableField("SUCCESS_RATE")
    private BigDecimal successRate;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowCode() {
        return workflowCode;
    }

    public void setWorkflowCode(String workflowCode) {
        this.workflowCode = workflowCode;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public String getWorkflowDefinition() {
        return workflowDefinition;
    }

    public void setWorkflowDefinition(String workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    public String getNodeConfig() {
        return nodeConfig;
    }

    public void setNodeConfig(String nodeConfig) {
        this.nodeConfig = nodeConfig;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getWorkflowDescription() {
        return workflowDescription;
    }

    public void setWorkflowDescription(String workflowDescription) {
        this.workflowDescription = workflowDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAllowParallel() {
        return allowParallel;
    }

    public void setAllowParallel(Integer allowParallel) {
        this.allowParallel = allowParallel;
    }

    public Integer getAllowSkip() {
        return allowSkip;
    }

    public void setAllowSkip(Integer allowSkip) {
        this.allowSkip = allowSkip;
    }

    public Integer getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Integer autoStart) {
        this.autoStart = autoStart;
    }

    public Integer getNotifyOnStart() {
        return notifyOnStart;
    }

    public void setNotifyOnStart(Integer notifyOnStart) {
        this.notifyOnStart = notifyOnStart;
    }

    public Integer getNotifyOnComplete() {
        return notifyOnComplete;
    }

    public void setNotifyOnComplete(Integer notifyOnComplete) {
        this.notifyOnComplete = notifyOnComplete;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    public Integer getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Integer avgDuration) {
        this.avgDuration = avgDuration;
    }

    public BigDecimal getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }
}

