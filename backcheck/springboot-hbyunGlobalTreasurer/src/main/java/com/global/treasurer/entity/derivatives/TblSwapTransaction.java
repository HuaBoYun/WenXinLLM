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
 * 掉期交易实体类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_SWAP_TRANSACTION")
public class TblSwapTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "TRANSACTION_ID", type = IdType.AUTO)
    private Long transactionId;

    /**
     * 合约编号
     */
    @TableField(value = "CONTRACT_CODE")
    private String contractCode;

    /**
     * 掉期类型(INTEREST_RATE-利率掉期, CURRENCY-货币掉期, COMMODITY-商品掉期, CDS-信用违约掉期)
     */
    @TableField(value = "SWAP_TYPE")
    private String swapType;

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
     * 生效日期
     */
    @TableField(value = "EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 到期日期
     */
    @TableField(value = "MATURITY_DATE")
    private Date maturityDate;

    /**
     * 固定利率
     */
    @TableField(value = "FIXED_RATE")
    private BigDecimal fixedRate;

    /**
     * 浮动利率基准(SHIBOR, LPR, LIBOR, SOFR)
     */
    @TableField(value = "FLOATING_RATE_BASIS")
    private String floatingRateBasis;

    /**
     * 利差
     */
    @TableField(value = "SPREAD")
    private BigDecimal spread;

    /**
     * 支付频率(MONTHLY, QUARTERLY, SEMI_ANNUAL, ANNUAL)
     */
    @TableField(value = "PAYMENT_FREQUENCY")
    private String paymentFrequency;

    /**
     * 计息基准(ACT_360, ACT_365, 30_360)
     */
    @TableField(value = "DAY_COUNT_BASIS")
    private String dayCountBasis;

    /**
     * 支付方向(PAY_FIXED-支付固定/收取浮动, RECEIVE_FIXED-收取固定/支付浮动)
     */
    @TableField(value = "PAY_DIRECTION")
    private String payDirection;

    /**
     * 支付币种(货币掉期专用)
     */
    @TableField(value = "PAY_CURRENCY")
    private String payCurrency;

    /**
     * 收取币种(货币掉期专用)
     */
    @TableField(value = "RECEIVE_CURRENCY")
    private String receiveCurrency;

    /**
     * 支付本金(货币掉期专用)
     */
    @TableField(value = "PAY_NOTIONAL")
    private BigDecimal payNotional;

    /**
     * 收取本金(货币掉期专用)
     */
    @TableField(value = "RECEIVE_NOTIONAL")
    private BigDecimal receiveNotional;

    /**
     * 初始汇率(货币掉期专用)
     */
    @TableField(value = "INITIAL_EXCHANGE_RATE")
    private BigDecimal initialExchangeRate;

    /**
     * 是否交换本金(货币掉期专用)
     */
    @TableField(value = "EXCHANGE_PRINCIPAL")
    private String exchangePrincipal;

    /**
     * 当前估值
     */
    @TableField(value = "CURRENT_VALUE")
    private BigDecimal currentValue;

    /**
     * 下次支付日期
     */
    @TableField(value = "NEXT_PAYMENT_DATE")
    private Date nextPaymentDate;

    /**
     * 状态(PENDING-待生效, ACTIVE-生效中, TERMINATED-已终止, MATURED-已到期, CANCELLED-已取消)
     */
    @TableField(value = "STATUS")
    private String status;

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
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY")
    private String updateBy;

    /**
     * 机构ID
     */
    @TableField(value = "ORG_ID")
    private Long orgId;

    /**
     * 删除标志(0-正常, 1-删除)
     */
    @TableField(value = "DEL_FLAG")
    private String delFlag;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getSwapType() { return swapType; }
    public void setSwapType(String swapType) { this.swapType = swapType; }
    public BigDecimal getNotionalAmount() { return notionalAmount; }
    public void setNotionalAmount(BigDecimal notionalAmount) { this.notionalAmount = notionalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public BigDecimal getFixedRate() { return fixedRate; }
    public void setFixedRate(BigDecimal fixedRate) { this.fixedRate = fixedRate; }
    public String getFloatingRateBasis() { return floatingRateBasis; }
    public void setFloatingRateBasis(String floatingRateBasis) { this.floatingRateBasis = floatingRateBasis; }
    public BigDecimal getSpread() { return spread; }
    public void setSpread(BigDecimal spread) { this.spread = spread; }
    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    public String getDayCountBasis() { return dayCountBasis; }
    public void setDayCountBasis(String dayCountBasis) { this.dayCountBasis = dayCountBasis; }
    public String getPayDirection() { return payDirection; }
    public void setPayDirection(String payDirection) { this.payDirection = payDirection; }
    public String getPayCurrency() { return payCurrency; }
    public void setPayCurrency(String payCurrency) { this.payCurrency = payCurrency; }
    public String getReceiveCurrency() { return receiveCurrency; }
    public void setReceiveCurrency(String receiveCurrency) { this.receiveCurrency = receiveCurrency; }
    public BigDecimal getPayNotional() { return payNotional; }
    public void setPayNotional(BigDecimal payNotional) { this.payNotional = payNotional; }
    public BigDecimal getReceiveNotional() { return receiveNotional; }
    public void setReceiveNotional(BigDecimal receiveNotional) { this.receiveNotional = receiveNotional; }
    public BigDecimal getInitialExchangeRate() { return initialExchangeRate; }
    public void setInitialExchangeRate(BigDecimal initialExchangeRate) { this.initialExchangeRate = initialExchangeRate; }
    public String getExchangePrincipal() { return exchangePrincipal; }
    public void setExchangePrincipal(String exchangePrincipal) { this.exchangePrincipal = exchangePrincipal; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public Date getNextPaymentDate() { return nextPaymentDate; }
    public void setNextPaymentDate(Date nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCounterparty() { return counterparty; }
    public void setCounterparty(String counterparty) { this.counterparty = counterparty; }
    public String getCounterpartyRating() { return counterpartyRating; }
    public void setCounterpartyRating(String counterpartyRating) { this.counterpartyRating = counterpartyRating; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}
