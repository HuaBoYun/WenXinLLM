package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 授信申请DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class CreditApplicationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applicationId;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 授信类型 */
    @NotBlank(message = "授信类型不能为空")
    private String creditType;

    /** 申请授信金额 */
    @NotNull(message = "申请授信金额不能为空")
    private BigDecimal creditAmount;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 授信期限 */
    @NotNull(message = "授信期限不能为空")
    private Integer creditPeriod;

    /** 期限单位 */
    @NotBlank(message = "期限单位不能为空")
    private String periodUnit;

    /** 借款用途 */
    private String purpose;

    /** 担保方式 */
    private String guaranteeMethod;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCreditType() { return creditType; }
    public void setCreditType(String creditType) { this.creditType = creditType; }
    public BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getCreditPeriod() { return creditPeriod; }
    public void setCreditPeriod(Integer creditPeriod) { this.creditPeriod = creditPeriod; }
    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getGuaranteeMethod() { return guaranteeMethod; }
    public void setGuaranteeMethod(String guaranteeMethod) { this.guaranteeMethod = guaranteeMethod; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

