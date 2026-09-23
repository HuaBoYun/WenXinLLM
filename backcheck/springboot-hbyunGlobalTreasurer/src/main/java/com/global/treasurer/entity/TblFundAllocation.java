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
 * 资金下拨实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_ALLOCATION")
@ApiModel(value = "TblFundAllocation", description = "资金下拨实体")
public class TblFundAllocation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ALLOCATION_ID", type = IdType.AUTO)
    @ApiModelProperty("下拨ID")
    private Long allocationId;

    @TableField("ALLOCATION_CODE")
    @ApiModelProperty("下拨编码")
    private String allocationCode;

    @TableField("ALLOCATION_NAME")
    @ApiModelProperty("下拨名称")
    private String allocationName;

    @TableField("POOL_ID")
    @ApiModelProperty("资金池ID")
    private Long poolId;

    @TableField("SOURCE_ACCOUNT_ID")
    @ApiModelProperty("源账户ID")
    private Long sourceAccountId;

    @TableField("SOURCE_ACCOUNT_NAME")
    @ApiModelProperty("源账户名称")
    private String sourceAccountName;

    @TableField("TARGET_ACCOUNT_ID")
    @ApiModelProperty("目标账户ID")
    private Long targetAccountId;

    @TableField("TARGET_ACCOUNT_NAME")
    @ApiModelProperty("目标账户名称")
    private String targetAccountName;

    @TableField("ALLOCATION_AMOUNT")
    @ApiModelProperty("下拨金额")
    private BigDecimal allocationAmount;

    @TableField("ALLOCATION_STATUS")
    @ApiModelProperty("下拨状态(PENDING/EXECUTING/COMPLETED/CANCELLED/FAILED)")
    private String allocationStatus;

    @TableField("APPROVAL_STATUS")
    @ApiModelProperty("审批状态(PENDING/APPROVED/REJECTED)")
    private String approvalStatus;

    @TableField("APPROVER_ID")
    @ApiModelProperty("审批人ID")
    private String approverId;

    @TableField("APPROVER_NAME")
    @ApiModelProperty("审批人姓名")
    private String approverName;

    @TableField("APPROVAL_TIME")
    @ApiModelProperty("审批时间")
    private Date approvalTime;

    @TableField("APPROVAL_OPINION")
    @ApiModelProperty("审批意见")
    private String approvalOpinion;

    @TableField("EXECUTION_TIME")
    @ApiModelProperty("执行时间")
    private Date executionTime;

    @TableField("TRANSACTION_ID")
    @ApiModelProperty("交易ID")
    private String transactionId;

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

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    public Long getAllocationId() { return allocationId; }
    public void setAllocationId(Long allocationId) { this.allocationId = allocationId; }
    public String getAllocationCode() { return allocationCode; }
    public void setAllocationCode(String allocationCode) { this.allocationCode = allocationCode; }
    public String getAllocationName() { return allocationName; }
    public void setAllocationName(String allocationName) { this.allocationName = allocationName; }
    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public Long getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(Long sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public String getSourceAccountName() { return sourceAccountName; }
    public void setSourceAccountName(String sourceAccountName) { this.sourceAccountName = sourceAccountName; }
    public Long getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(Long targetAccountId) { this.targetAccountId = targetAccountId; }
    public String getTargetAccountName() { return targetAccountName; }
    public void setTargetAccountName(String targetAccountName) { this.targetAccountName = targetAccountName; }
    public BigDecimal getAllocationAmount() { return allocationAmount; }
    public void setAllocationAmount(BigDecimal allocationAmount) { this.allocationAmount = allocationAmount; }
    public String getAllocationStatus() { return allocationStatus; }
    public void setAllocationStatus(String allocationStatus) { this.allocationStatus = allocationStatus; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApproverId() { return approverId; }
    public void setApproverId(String approverId) { this.approverId = approverId; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public Date getExecutionTime() { return executionTime; }
    public void setExecutionTime(Date executionTime) { this.executionTime = executionTime; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
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
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
