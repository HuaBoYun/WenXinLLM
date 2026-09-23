package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 远期交易实体类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FORWARD_TRANSACTION")
public class TblForwardTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID - 使用数据库自增
     */
    @TableId(value = "TRANSACTION_ID", type = IdType.AUTO)
    private Long transactionId;

    /**
     * 合约编号
     */
    @TableField(value = "CONTRACT_CODE")
    private String contractCode;

    /**
     * 交易类型(FX_FORWARD-远期外汇, RATE_FORWARD-远期利率, COMMODITY_FORWARD-远期商品)
     */
    @TableField(value = "TRANSACTION_TYPE")
    private String transactionType;

    /**
     * 标的资产
     */
    @TableField(value = "UNDERLYING_ASSET")
    private String underlyingAsset;

    /**
     * 名义本金
     */
    @TableField(value = "NOTIONAL_AMOUNT")
    private BigDecimal notionalAmount;

    /**
     * 币种
     */
    @TableField(value = "CURRENCY")
    private String currency;

    /**
     * 远期价格
     */
    @TableField(value = "FORWARD_PRICE")
    private BigDecimal forwardPrice;

    /**
     * 交易日期
     */
    @TableField(value = "TRADE_DATE")
    private Date tradeDate;

    /**
     * 到期日期
     */
    @TableField(value = "MATURITY_DATE")
    private Date maturityDate;

    /**
     * 当前估值
     */
    @TableField(value = "CURRENT_VALUE")
    private BigDecimal currentValue;

    /**
     * 损益
     */
    @TableField(value = "PNL")
    private BigDecimal pnl;

    /**
     * 交易对手
     */
    @TableField(value = "COUNTERPARTY")
    private String counterparty;

    /**
     * 对手评级
     */
    @TableField(value = "COUNTERPARTY_RATING")
    private String counterpartyRating;

    /**
     * 交易员
     */
    @TableField(value = "TRADER")
    private String trader;

    /**
     * 销售员
     */
    @TableField(value = "SALESPERSON")
    private String salesperson;

    /**
     * 风险限额
     */
    @TableField(value = "RISK_LIMIT")
    private BigDecimal riskLimit;

    /**
     * 保证金要求
     */
    @TableField(value = "MARGIN_REQUIREMENT")
    private BigDecimal marginRequirement;

    /**
     * 状态(PENDING-待生效, ACTIVE-生效中, EXECUTED-已执行, SETTLED-已交割, CANCELLED-已取消)
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY")
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY")
    private Long updateBy;

    /**
     * 删除标志(0-正常, 1-删除)
     */
    @TableField(value = "DEL_FLAG")
    private Integer delFlag;

    /**
     * 机构ID
     */
    @TableField(value = "ORG_ID")
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getUnderlyingAsset() { return underlyingAsset; }
    public void setUnderlyingAsset(String underlyingAsset) { this.underlyingAsset = underlyingAsset; }
    public BigDecimal getNotionalAmount() { return notionalAmount; }
    public void setNotionalAmount(BigDecimal notionalAmount) { this.notionalAmount = notionalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getForwardPrice() { return forwardPrice; }
    public void setForwardPrice(BigDecimal forwardPrice) { this.forwardPrice = forwardPrice; }
    public Date getTradeDate() { return tradeDate; }
    public void setTradeDate(Date tradeDate) { this.tradeDate = tradeDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public BigDecimal getPnl() { return pnl; }
    public void setPnl(BigDecimal pnl) { this.pnl = pnl; }
    public String getCounterparty() { return counterparty; }
    public void setCounterparty(String counterparty) { this.counterparty = counterparty; }
    public String getCounterpartyRating() { return counterpartyRating; }
    public void setCounterpartyRating(String counterpartyRating) { this.counterpartyRating = counterpartyRating; }
    public String getTrader() { return trader; }
    public void setTrader(String trader) { this.trader = trader; }
    public String getSalesperson() { return salesperson; }
    public void setSalesperson(String salesperson) { this.salesperson = salesperson; }
    public BigDecimal getRiskLimit() { return riskLimit; }
    public void setRiskLimit(BigDecimal riskLimit) { this.riskLimit = riskLimit; }
    public BigDecimal getMarginRequirement() { return marginRequirement; }
    public void setMarginRequirement(BigDecimal marginRequirement) { this.marginRequirement = marginRequirement; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
