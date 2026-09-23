package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 担保合同实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GUARANTEE_CONTRACT")
public class TblGuaranteeContract implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 合同ID - 使用数据库自增，并序列化为字符串避免JavaScript精度丢失 */
    @TableId(value = "CONTRACT_ID", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long contractId;

    /** 合同编号 */
    @TableField("CONTRACT_NO")
    private String contractNo;

    /** 合同类型(GUARANTEE-保证合同,MORTGAGE-抵押合同,PLEDGE-质押合同) */
    @TableField("CONTRACT_TYPE")
    private String contractType;

    /** 担保申请ID */
    @TableField("GUARANTEE_APPLICATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long guaranteeApplicationId;

    /** 保证人 */
    @TableField("GUARANTOR_COMPANY")
    private String guarantorCompany;

    /** 受益人 */
    @TableField("BENEFICIARY_COMPANY")
    private String beneficiaryCompany;

    /** 担保金额 */
    @TableField("GUARANTEE_AMOUNT")
    private BigDecimal guaranteeAmount;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /** 担保期限 */
    @TableField("GUARANTEE_PERIOD")
    private Integer guaranteePeriod;

    /** 起始日期 */
    @TableField("START_DATE")
    private Date startDate;

    /** 到期日期 */
    @TableField("END_DATE")
    private Date endDate;

    /** 担保对象 */
    @TableField("GUARANTEE_OBJECT")
    private String guaranteeObject;

    /** 担保方式 */
    @TableField("GUARANTEE_TYPE")
    private String guaranteeType;

    /** 合同状态(DRAFT-草稿,PENDING-待审批,SIGNED-已签署,EFFECTIVE-生效中,TERMINATED-已终止,CANCELLED-已取消) */
    @TableField("CONTRACT_STATUS")
    private String contractStatus;

    /** 签署日期 */
    @TableField("SIGNING_DATE")
    private Date signingDate;

    /** 生效日期 */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /** 终止日期 */
    @TableField("TERMINATION_DATE")
    private Date terminationDate;

    /** 审批日期 */
    @TableField("APPROVAL_DATE")
    private Date approvalDate;

    /** 审批人ID */
    @TableField("APPROVER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long approverId;

    /** 审批人姓名 */
    @TableField("APPROVER_NAME")
    private String approverName;

    /** 审批意见 */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

    /** 合同附件 */
    @TableField("CONTRACT_FILE")
    private String contractFile;

    /** 公司ID */
    @TableField("COMPANY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;

    /** 公司名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    /** 担保方式(GUARANTEE-保证,MORTGAGE-抵押,PLEDGE-质押) */
    @TableField("GUARANTEE_MODE")
    private String guaranteeMode;

    /** 风险等级(LOW-低风险,MEDIUM-中风险,HIGH-高风险,CRITICAL-严重风险) */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /** 担保比例(百分比) */
    @TableField("GUARANTEE_RATIO")
    private BigDecimal guaranteeRatio;

    /** 担保范围 */
    @TableField("GUARANTEE_SCOPE")
    private String guaranteeScope;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public Long getGuaranteeApplicationId() { return guaranteeApplicationId; }
    public void setGuaranteeApplicationId(Long guaranteeApplicationId) { this.guaranteeApplicationId = guaranteeApplicationId; }
    public String getGuarantorCompany() { return guarantorCompany; }
    public void setGuarantorCompany(String guarantorCompany) { this.guarantorCompany = guarantorCompany; }
    public String getBeneficiaryCompany() { return beneficiaryCompany; }
    public void setBeneficiaryCompany(String beneficiaryCompany) { this.beneficiaryCompany = beneficiaryCompany; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getGuaranteePeriod() { return guaranteePeriod; }
    public void setGuaranteePeriod(Integer guaranteePeriod) { this.guaranteePeriod = guaranteePeriod; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getGuaranteeObject() { return guaranteeObject; }
    public void setGuaranteeObject(String guaranteeObject) { this.guaranteeObject = guaranteeObject; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public Date getSigningDate() { return signingDate; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getTerminationDate() { return terminationDate; }
    public void setTerminationDate(Date terminationDate) { this.terminationDate = terminationDate; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public String getApprovalComments() { return approvalComments; }
    public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }
    public String getContractFile() { return contractFile; }
    public void setContractFile(String contractFile) { this.contractFile = contractFile; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getGuaranteeMode() { return guaranteeMode; }
    public void setGuaranteeMode(String guaranteeMode) { this.guaranteeMode = guaranteeMode; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getGuaranteeRatio() { return guaranteeRatio; }
    public void setGuaranteeRatio(BigDecimal guaranteeRatio) { this.guaranteeRatio = guaranteeRatio; }
    public String getGuaranteeScope() { return guaranteeScope; }
    public void setGuaranteeScope(String guaranteeScope) { this.guaranteeScope = guaranteeScope; }
}
