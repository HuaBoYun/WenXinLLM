package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@TableName("TBL_BANK_WEALTH_INVESTMENT")
@ApiModel(value = "TblBankWealthInvestment", description = "银行理财投资")
public class TblBankWealthInvestment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "INVESTMENT_ID", type = IdType.AUTO)
    @ApiModelProperty("投资ID")
    private Long bankWealthInvestmentId;

    @TableField("INVESTMENT_NO")
    @ApiModelProperty("投资编号")
    private String investmentNo;

    @TableField("PRODUCT_ID")
    @ApiModelProperty("产品ID")
    private Long productId;

    @TableField("PRODUCT_CODE")
    @ApiModelProperty("产品代码")
    private String productCode;

    @TableField("PRODUCT_NAME")
    @ApiModelProperty("产品名称")
    private String productName;

    @TableField("BANK_NAME")
    @ApiModelProperty("银行名称")
    private String bankCode;

    @TableField("INVESTMENT_AMOUNT")
    @ApiModelProperty("投资金额")
    private BigDecimal investmentAmount;

    @TableField("EXPECTED_RETURN_RATE")
    @ApiModelProperty("预期收益率")
    private BigDecimal expectedReturnRate;

    @TableField("ACTUAL_RETURN_RATE")
    @ApiModelProperty("实际收益率")
    private BigDecimal actualReturnRate;

    @TableField("EXPECTED_RETURN")
    @ApiModelProperty("预期收益")
    private BigDecimal expectedReturn;

    @TableField("ACTUAL_RETURN")
    @ApiModelProperty("实际收益")
    private BigDecimal actualReturn;

    @TableField("INVESTMENT_TERM")
    @ApiModelProperty("投资期限(天)")
    private Integer investmentTerm;

    @TableField("VALUE_DATE")
    @ApiModelProperty("起息日")
    private Date investmentDate;

    @TableField("MATURITY_DATE")
    @ApiModelProperty("到期日")
    private Date maturityDate;

    @TableField("REDEMPTION_DATE")
    @ApiModelProperty("赎回日")
    private Date redeemDate;

    @TableField("RISK_LEVEL")
    @ApiModelProperty("风险等级")
    private String riskLevel;

    @TableField("INVESTMENT_STATUS")
    @ApiModelProperty("投资状态")
    private String investmentStatus;

    @TableField("CURRENCY_CODE")
    @ApiModelProperty("币种代码")
    private String currencyCode;

    @TableField("PLAN_ID")
    @ApiModelProperty("投资计划ID")
    private Long planId;

    @TableField("PLAN_NO")
    @ApiModelProperty("投资计划编号")
    private String planNo;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private Long companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("DELETE_FLAG")
    @ApiModelProperty("删除标志")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    @ApiModelProperty("创建人ID")
    private Long createUser;

    @TableField("CREATED_BY_NAME")
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @TableField("CREATED_TIME")
    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @TableField("UPDATED_BY")
    @ApiModelProperty("更新人ID")
    private Long updateUser;

    @TableField("UPDATED_BY_NAME")
    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    @TableField("UPDATED_TIME")
    @ApiModelProperty("更新时间")
    private Timestamp updateTime;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    // 用于兼容前端字段的getter和setter
    public BigDecimal getCurrentValue() {
        return actualReturn != null ? investmentAmount.add(actualReturn) : investmentAmount;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        // currentValue不存储，通过actualReturn计算
        // 这里不做任何操作，只保留方法以避免MyBatis报错
    }

    public BigDecimal getRedeemAmount() {
        return actualReturn;
    }

    public void setRedeemAmount(BigDecimal redeemAmount) {
        this.actualReturn = redeemAmount;
    }

    // 缺失的setter和getter方法
    public void setActualReturnRate(BigDecimal actualReturnRate) {
        this.actualReturnRate = actualReturnRate;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public void setExpectedReturn(BigDecimal expectedReturn) {
        this.expectedReturn = expectedReturn;
    }

    public void setActualReturn(BigDecimal actualReturn) {
        this.actualReturn = actualReturn;
    }

    public void setInvestmentDate(Date investmentDate) {
        this.investmentDate = investmentDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public void setBankWealthInvestmentId(Long bankWealthInvestmentId) {
        this.bankWealthInvestmentId = bankWealthInvestmentId;
    }

    // 缺失的getter方法
    public BigDecimal getInvestmentAmount() {
        return this.investmentAmount;
    }

    public BigDecimal getExpectedReturn() {
        return this.expectedReturn;
    }

    public BigDecimal getActualReturn() {
        return this.actualReturn;
    }

    public String getInvestmentStatus() {
        return this.investmentStatus;
    }

    public Long getBankWealthInvestmentId() {
        return this.bankWealthInvestmentId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public String getInvestmentNo() {
        return this.investmentNo;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public BigDecimal getExpectedReturnRate() {
        return this.expectedReturnRate;
    }

    public BigDecimal getActualReturnRate() {
        return this.actualReturnRate;
    }

    public String getRiskLevel() {
        return this.riskLevel;
    }

    public Date getInvestmentDate() {
        return this.investmentDate;
    }

    public Date getMaturityDate() {
        return this.maturityDate;
    }

    public Date getRedeemDate() {
        return this.redeemDate;
    }

    public Long getPlanId() {
        return this.planId;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public void setInvestmentNo(String investmentNo) {
        this.investmentNo = investmentNo;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }
    public void setInvestmentStatus(String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }
    public void setExpectedReturnRate(BigDecimal expectedReturnRate) {
        this.expectedReturnRate = expectedReturnRate;
    }
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    // 兼容性getter方法 - 用于Controller导出等功能
    public String getIssuingBank() {
        return this.bankCode;
    }

    public String getProductType() {
        return "银行理财"; // 固定产品类型
    }

    public String getProductStatus() {
        return this.investmentStatus;
    }

    public Date getPurchaseDate() {
        return this.investmentDate;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getInvestmentTerm() {
        return this.investmentTerm;
    }

    public void setInvestmentTerm(Integer investmentTerm) {
        this.investmentTerm = investmentTerm;
    }
}
