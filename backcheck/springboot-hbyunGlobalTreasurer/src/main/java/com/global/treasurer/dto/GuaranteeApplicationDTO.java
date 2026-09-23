package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 担保申请DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class GuaranteeApplicationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applicationId;

    /** 担保类型 */
    @NotBlank(message = "担保类型不能为空")
    private String guaranteeType;

    /** 担保金额 */
    @NotNull(message = "担保金额不能为空")
    private BigDecimal guaranteeAmount;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 担保期限 */
    @NotNull(message = "担保期限不能为空")
    private Integer guaranteePeriod;

    /** 担保方 */
    @NotBlank(message = "担保方不能为空")
    private String guarantorCompany;

    /** 受益方 */
    @NotBlank(message = "受益方不能为空")
    private String beneficiaryCompany;

    /** 担保对象 */
    private String guaranteeObject;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
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
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
