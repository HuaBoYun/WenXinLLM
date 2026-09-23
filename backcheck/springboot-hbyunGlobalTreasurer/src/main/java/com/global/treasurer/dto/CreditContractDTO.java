package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 授信合同DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class CreditContractDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 合同ID */
    private Long contractId;

    /** 合同编号 */
    private String contractNo;

    /** 授信申请ID */
    private Long creditApplicationId;

    /** 银行代码 */
    @NotBlank(message = "银行代码不能为空")
    private String bankCode;

    /** 银行名称 */
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    /** 授信额度 */
    @NotNull(message = "授信额度不能为空")
    private BigDecimal creditLimit;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 授信期限 */
    @NotNull(message = "授信期限不能为空")
    private Integer creditPeriod;

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

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Long getCreditApplicationId() { return creditApplicationId; }
    public void setCreditApplicationId(Long creditApplicationId) { this.creditApplicationId = creditApplicationId; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getCreditPeriod() { return creditPeriod; }
    public void setCreditPeriod(Integer creditPeriod) { this.creditPeriod = creditPeriod; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getGuaranteeMethod() { return guaranteeMethod; }
    public void setGuaranteeMethod(String guaranteeMethod) { this.guaranteeMethod = guaranteeMethod; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
