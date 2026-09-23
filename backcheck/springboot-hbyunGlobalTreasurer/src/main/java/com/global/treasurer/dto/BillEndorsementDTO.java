package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据背书DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillEndorsementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 背书ID */
    private Long endorsementId;

    /** 背书编号 */
    private String endorsementNumber;

    /** 票据编号 */
    @NotBlank(message = "票据编号不能为空")
    private String billNumber;

    /** 背书类型 */
    @NotBlank(message = "背书类型不能为空")
    private String endorsementType;

    /** 票据金额 */
    @NotNull(message = "票据金额不能为空")
    private BigDecimal billAmount;

    /** 背书人名称 */
    @NotBlank(message = "背书人名称不能为空")
    private String endorserName;

    /** 背书人账号 */
    private String endorserAccount;

    /** 被背书人名称 */
    @NotBlank(message = "被背书人名称不能为空")
    private String endorseeName;

    /** 被背书人账号 */
    private String endorseeAccount;

    /** 被背书人开户行 */
    private String endorseeBank;

    /** 联系电话 */
    private String contactPhone;

    /** 申请日期 */
    private Date applicationDate;

    /** 预计执行日期 */
    private Date expectedExecutionDate;

    /** 背书用途 */
    private String endorsementPurpose;

    /** 是否连续背书 */
    private Integer isContinuous;

    /** 风险等级 */
    private String riskLevel;

    /** 备注 */
    private String remark;

    /** 公司ID */
    private Long companyId;

    /** 部门ID */
    private Long deptId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getEndorsementId() { return endorsementId; }
    public void setEndorsementId(Long endorsementId) { this.endorsementId = endorsementId; }
    public String getEndorsementNumber() { return endorsementNumber; }
    public void setEndorsementNumber(String endorsementNumber) { this.endorsementNumber = endorsementNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getEndorsementType() { return endorsementType; }
    public void setEndorsementType(String endorsementType) { this.endorsementType = endorsementType; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getEndorserName() { return endorserName; }
    public void setEndorserName(String endorserName) { this.endorserName = endorserName; }
    public String getEndorserAccount() { return endorserAccount; }
    public void setEndorserAccount(String endorserAccount) { this.endorserAccount = endorserAccount; }
    public String getEndorseeName() { return endorseeName; }
    public void setEndorseeName(String endorseeName) { this.endorseeName = endorseeName; }
    public String getEndorseeAccount() { return endorseeAccount; }
    public void setEndorseeAccount(String endorseeAccount) { this.endorseeAccount = endorseeAccount; }
    public String getEndorseeBank() { return endorseeBank; }
    public void setEndorseeBank(String endorseeBank) { this.endorseeBank = endorseeBank; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getExpectedExecutionDate() { return expectedExecutionDate; }
    public void setExpectedExecutionDate(Date expectedExecutionDate) { this.expectedExecutionDate = expectedExecutionDate; }
    public String getEndorsementPurpose() { return endorsementPurpose; }
    public void setEndorsementPurpose(String endorsementPurpose) { this.endorsementPurpose = endorsementPurpose; }
    public Integer getIsContinuous() { return isContinuous; }
    public void setIsContinuous(Integer isContinuous) { this.isContinuous = isContinuous; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
}
