package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据贴现DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillDiscountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 贴现ID */
    private Long discountId;

    /** 贴现编号 */
    private String discountNumber;

    /** 票据编号 */
    @NotBlank(message = "票据编号不能为空")
    private String billNumber;

    /** 票据金额 */
    @NotNull(message = "票据金额不能为空")
    private BigDecimal billAmount;

    /** 贴现利率 */
    @NotNull(message = "贴现利率不能为空")
    private BigDecimal discountRate;

    /** 贴现天数 */
    private Integer discountPeriod;

    /** 贴现利息 */
    private BigDecimal discountInterest;

    /** 贴现金额 */
    private BigDecimal discountAmount;

    /** 贴现银行 */
    @NotBlank(message = "贴现银行不能为空")
    private String discountBank;

    /** 申请日期 */
    private Date applicationDate;

    /** 预计贴现日期 */
    private Date expectedDiscountDate;

    /** 备注 */
    private String remark;

    /** 公司ID */
    private Long companyId;

    /** 部门ID */
    private Long deptId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDiscountId() { return discountId; }
    public void setDiscountId(Long discountId) { this.discountId = discountId; }
    public String getDiscountNumber() { return discountNumber; }
    public void setDiscountNumber(String discountNumber) { this.discountNumber = discountNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public Integer getDiscountPeriod() { return discountPeriod; }
    public void setDiscountPeriod(Integer discountPeriod) { this.discountPeriod = discountPeriod; }
    public BigDecimal getDiscountInterest() { return discountInterest; }
    public void setDiscountInterest(BigDecimal discountInterest) { this.discountInterest = discountInterest; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getDiscountBank() { return discountBank; }
    public void setDiscountBank(String discountBank) { this.discountBank = discountBank; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getExpectedDiscountDate() { return expectedDiscountDate; }
    public void setExpectedDiscountDate(Date expectedDiscountDate) { this.expectedDiscountDate = expectedDiscountDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

}
