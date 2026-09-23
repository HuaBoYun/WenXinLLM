package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 授信额度DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class CreditLimitDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 额度ID */
    private Long limitId;

    /** 合同ID */
    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 额度类型 */
    @NotBlank(message = "额度类型不能为空")
    private String limitType;

    /** 总额度 */
    @NotNull(message = "总额度不能为空")
    private BigDecimal totalLimit;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 起始日期 */
    @NotNull(message = "起始日期不能为空")
    private Date startDate;

    /** 到期日期 */
    @NotNull(message = "到期日期不能为空")
    private Date endDate;

    /** 利率 */
    private BigDecimal interestRate;

    /** 担保方式 */
    private String guaranteeMethod;

    /** 额度用途 */
    private String purpose;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLimitId() { return limitId; }
    public void setLimitId(Long limitId) { this.limitId = limitId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getLimitType() { return limitType; }
    public void setLimitType(String limitType) { this.limitType = limitType; }
    public BigDecimal getTotalLimit() { return totalLimit; }
    public void setTotalLimit(BigDecimal totalLimit) { this.totalLimit = totalLimit; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getGuaranteeMethod() { return guaranteeMethod; }
    public void setGuaranteeMethod(String guaranteeMethod) { this.guaranteeMethod = guaranteeMethod; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
