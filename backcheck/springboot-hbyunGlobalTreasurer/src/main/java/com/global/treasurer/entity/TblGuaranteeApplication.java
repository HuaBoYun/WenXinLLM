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
 * 担保申请实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GUARANTEE_APPLICATION")
public class TblGuaranteeApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 申请ID - 使用字符串序列化避免JavaScript精度丢失 */
    @TableId(value = "APPLICATION_ID", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    /** 申请编号 */
    @TableField("APPLICATION_NO")
    private String applicationNo;

    /** 担保类型(GENERAL-一般保证,JOINT-连带责任保证) */
    @TableField("GUARANTEE_TYPE")
    private String guaranteeType;

    /** 担保金额 - 映射到数据库的 APPLY_AMOUNT 列 */
    @TableField("APPLY_AMOUNT")
    private BigDecimal guaranteeAmount;

    /** 申请金额 */
    @TableField(exist = false)
    private BigDecimal applyAmount;

    /** 批准金额 */
    @TableField("APPROVED_AMOUNT")
    private BigDecimal approvedAmount;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /** 担保期限 - 数据库中不存在此列 */
    @TableField(exist = false)
    private Integer guaranteePeriod;

    /** 担保方ID */
    @TableField("GUARANTOR_ID")
    private Long guarantorId;

    /** 担保方名称 */
    @TableField("GUARANTOR_NAME")
    private String guarantorName;

    /** 担保方 - 数据库中不存在此列 */
    @TableField(exist = false)
    private String guarantorCompany;

    /** 受益方 - 数据库中不存在此列 */
    @TableField(exist = false)
    private String beneficiaryCompany;

    /** 担保对象 - 数据库中不存在此列 */
    @TableField(exist = false)
    private String guaranteeObject;

    /** 状态 - 数据库中不存在此列 */
    @TableField(exist = false)
    private String status;

    /** 申请状态 */
    @TableField("APPLICATION_STATUS")
    private String applicationStatus;

    /** 申请日期 - 数据库中不存在此列 */
    @TableField(exist = false)
    private Date applicationDate;

    /** 申请日期(applyDate) */
    @TableField("APPLY_DATE")
    private Date applyDate;

    /** 申请时间(前端使用) - 数据库中不存在此列，由 APPLY_DATE 映射 */
    @TableField(exist = false)
    private Date applicationTime;

    /** 被担保方(前端使用) - 数据库中不存在此列，由 GUARANTOR_NAME 映射 */
    @TableField(exist = false)
    private String guaranteedParty;

    /** 预计开始日期 */
    @TableField("EXPECTED_START_DATE")
    private Date expectedStartDate;

    /** 预计结束日期 */
    @TableField("EXPECTED_END_DATE")
    private Date expectedEndDate;

    /** 申请人ID */
    @TableField("APPLICANT_ID")
    private Long applicantId;

    /** 申请人姓名 */
    @TableField("APPLICANT_NAME")
    private String applicantName;

    /** 公司ID - 数据库中不存在此列 */
    @TableField(exist = false)
    private Long companyId;

    /** 公司名称 - 数据库中不存在此列 */
    @TableField(exist = false)
    private String companyName;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 备注 - 数据库中可能不存在此列 */
    @TableField(exist = false)
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getGuaranteePeriod() { return guaranteePeriod; }
    public void setGuaranteePeriod(Integer guaranteePeriod) { this.guaranteePeriod = guaranteePeriod; }
    public String getGuarantorCompany() { return guarantorCompany; }
    public void setGuarantorCompany(String guarantorCompany) { this.guarantorCompany = guarantorCompany; }
    public String getBeneficiaryCompany() { return beneficiaryCompany; }
    public void setBeneficiaryCompany(String beneficiaryCompany) { this.beneficiaryCompany = beneficiaryCompany; }
    public String getGuaranteeObject() { return guaranteeObject; }
    public void setGuaranteeObject(String guaranteeObject) { this.guaranteeObject = guaranteeObject; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    // 新增字段的 getter/setter
    public BigDecimal getApplyAmount() { return applyAmount; }
    public void setApplyAmount(BigDecimal applyAmount) { this.applyAmount = applyAmount; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    public Long getGuarantorId() { return guarantorId; }
    public void setGuarantorId(Long guarantorId) { this.guarantorId = guarantorId; }
    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public Date getApplyDate() { return applyDate; }
    public void setApplyDate(Date applyDate) { this.applyDate = applyDate; }
    public Date getExpectedStartDate() { return expectedStartDate; }
    public void setExpectedStartDate(Date expectedStartDate) { this.expectedStartDate = expectedStartDate; }
    public Date getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(Date expectedEndDate) { this.expectedEndDate = expectedEndDate; }

    // 前端需要的字段 getter/setter
    public Date getApplicationTime() { return applicationTime; }
    public void setApplicationTime(Date applicationTime) { this.applicationTime = applicationTime; }
    public String getGuaranteedParty() { return guaranteedParty; }
    public void setGuaranteedParty(String guaranteedParty) { this.guaranteedParty = guaranteedParty; }

}
