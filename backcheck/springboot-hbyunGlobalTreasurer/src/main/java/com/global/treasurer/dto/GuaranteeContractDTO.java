package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 担保合同DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class GuaranteeContractDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 合同ID */
    private Long contractId;

    /** 合同编号 */
    private String contractNo;

    /** 合同类型 */
    @NotBlank(message = "合同类型不能为空")
    private String contractType;

    /** 担保申请ID */
    private Long guaranteeApplicationId;

    /** 保证人 */
    @NotBlank(message = "保证人不能为空")
    private String guarantorCompany;

    /** 受益人 */
    @NotBlank(message = "受益人不能为空")
    private String beneficiaryCompany;

    /** 担保金额 */
    @NotNull(message = "担保金额不能为空")
    private BigDecimal guaranteeAmount;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 担保期限 */
    @NotNull(message = "担保期限不能为空")
    private Integer guaranteePeriod;

    /** 起始日期 */
    @NotNull(message = "起始日期不能为空")
    private Date startDate;

    /** 到期日期 */
    @NotNull(message = "到期日期不能为空")
    private Date endDate;

    /** 担保对象 */
    private String guaranteeObject;

    /** 担保方式 */
    private String guaranteeType;

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
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
