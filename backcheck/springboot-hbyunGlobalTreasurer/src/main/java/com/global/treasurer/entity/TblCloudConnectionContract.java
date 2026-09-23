package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 云连接合同实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@TableName("TBL_CLOUD_CONNECTION_CONTRACT")
public class TblCloudConnectionContract implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同ID - 使用数据库自增
     */
    @TableId(value = "CONTRACT_ID", type = IdType.AUTO)
    @TableField("CONTRACT_ID")
    private Long contractId;

    /**
     * 合同编号
     */
    @TableField("CONTRACT_NUMBER")
    private String contractNumber;

    /**
     * 合同名称
     */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /**
     * 服务提供商
     */
    @TableField("SERVICE_PROVIDER")
    private String serviceProvider;

    /**
     * 服务类型
     */
    @TableField("SERVICE_TYPE")
    private String serviceType;

    /**
     * 合同金额
     */
    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 签署日期
     */
    @TableField("SIGN_DATE")
    private Date signDate;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 到期日期
     */
    @TableField("EXPIRY_DATE")
    private Date expiryDate;

    /**
     * 合同状态
     */
    @TableField("CONTRACT_STATUS")
    private String contractStatus;

    /**
     * 付款方式
     */
    @TableField("PAYMENT_METHOD")
    private String paymentMethod;

    /**
     * 付款周期
     */
    @TableField("PAYMENT_CYCLE")
    private String paymentCycle;

    /**
     * 服务级别协议(SLA)
     */
    @TableField("SLA")
    private String sla;

    /**
     * 合同文件路径
     */
    @TableField("CONTRACT_FILE_PATH")
    private String contractFilePath;

    /**
     * 合同描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 删除标志
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /**
     * 创建人ID
     */
    @TableField("CREATED_BY")
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATED_BY")
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 部门ID
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public void setIsEnabled(int isEnabled) { this.isEnabled = isEnabled; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public void setDeleteFlag(int deleteFlag) { this.deleteFlag = deleteFlag; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public String getServiceProvider() { return serviceProvider; }
    public void setServiceProvider(String serviceProvider) { this.serviceProvider = serviceProvider; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public BigDecimal getContractAmount() { return contractAmount; }
    public void setContractAmount(BigDecimal contractAmount) { this.contractAmount = contractAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getSignDate() { return signDate; }
    public void setSignDate(Date signDate) { this.signDate = signDate; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentCycle() { return paymentCycle; }
    public void setPaymentCycle(String paymentCycle) { this.paymentCycle = paymentCycle; }
    public String getSla() { return sla; }
    public void setSla(String sla) { this.sla = sla; }
    public String getContractFilePath() { return contractFilePath; }
    public void setContractFilePath(String contractFilePath) { this.contractFilePath = contractFilePath; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
}
