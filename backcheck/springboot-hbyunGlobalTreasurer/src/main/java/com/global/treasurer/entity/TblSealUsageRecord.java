package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 印鉴使用记录实体类
 * 对应数据库表: TBL_SEAL_USAGE_RECORD
 * 基于 01_create_seal_usage_record.sql 表结构
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
@TableName("TBL_SEAL_USAGE_RECORD")
public class TblSealUsageRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 记录编号
     */
    @TableField("RECORD_NUMBER")
    private String recordNumber;

    /**
     * 印鉴ID
     */
    @TableField("SEAL_ID")
    private Long sealId;

    /**
     * 印鉴编码
     */
    @TableField("SEAL_CODE")
    private String sealCode;

    /**
     * 印鉴名称
     */
    @TableField("SEAL_NAME")
    private String sealName;

    /**
     * 操作人ID
     */
    @TableField("OPERATOR_ID")
    private String operatorId;

    /**
     * 操作人姓名
     */
    @TableField("OPERATOR_NAME")
    private String operatorName;

    /**
     * 业务类型
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;

    /**
     * 业务单号
     */
    @TableField("BUSINESS_NUMBER")
    private String businessNumber;

    /**
     * 使用状态: PENDING-待审批, APPROVED-已审批, COMPLETED-已完成
     */
    @TableField("USAGE_STATUS")
    private String usageStatus;

    /**
     * 使用时间
     */
    @TableField("USAGE_TIME")
    private Date usageTime;

    /**
     * 使用说明
     */
    @TableField("USAGE_DESCRIPTION")
    private String usageDescription;

    /**
     * 使用金额
     */
    @TableField(exist = false)
    private java.math.BigDecimal usageAmount;

    /**
     * 审批人
     */
    @TableField("APPROVER")
    private String approver;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

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

    // Getter and Setter methods

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRecordNumber() { return recordNumber; }
    public void setRecordNumber(String recordNumber) { this.recordNumber = recordNumber; }

    public Long getSealId() { return sealId; }
    public void setSealId(Long sealId) { this.sealId = sealId; }

    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }

    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }

    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(String businessNumber) { this.businessNumber = businessNumber; }

    public String getUsageStatus() { return usageStatus; }
    public void setUsageStatus(String usageStatus) { this.usageStatus = usageStatus; }

    public Date getUsageTime() { return usageTime; }
    public void setUsageTime(Date usageTime) { this.usageTime = usageTime; }

    public String getUsageDescription() { return usageDescription; }
    public void setUsageDescription(String usageDescription) { this.usageDescription = usageDescription; }

    public java.math.BigDecimal getUsageAmount() { return usageAmount; }
    public void setUsageAmount(java.math.BigDecimal usageAmount) { this.usageAmount = usageAmount; }

    public String getApprover() { return approver; }
    public void setApprover(String approver) { this.approver = approver; }

    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
