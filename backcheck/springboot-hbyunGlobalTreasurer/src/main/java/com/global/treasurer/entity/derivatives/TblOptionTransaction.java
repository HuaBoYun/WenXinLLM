package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.math.BigDecimal;
import java.util.Date;

/**
 * 期权交易实体类
 * 对应数据库表: TBL_OPTION_TRANSACTION
 *
 * @author AI Developer
 * @date 2026-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_OPTION_TRANSACTION")
public class TblOptionTransaction {
    /**
     * 主键ID
     */
    @TableId(value = "TRANSACTION_ID", type = IdType.AUTO)
    private Long transactionId;

    /**
     * 合约编号
     */
    @TableField("CONTRACT_CODE")
    private String contractCode;

    /**
     * 期权类型(CALL-看涨期权, PUT-看跌期权)
     */
    @TableField("OPTION_TYPE")
    private String optionType;

    /**
     * 期权风格(EUROPEAN-欧式, AMERICAN-美式, ASIAN-亚式)
     */
    @TableField("OPTION_STYLE")
    private String optionStyle;

    /**
     * 标的资产
     */
    @TableField("UNDERLYING_ASSET")
    private String underlyingAsset;

    /**
     * 合约数量
     */
    @TableField("CONTRACT_SIZE")
    private BigDecimal contractSize;

    /**
     * 行权价格
     */
    @TableField("STRIKE_PRICE")
    private BigDecimal strikePrice;

    /**
     * 期权费
     */
    @TableField("PREMIUM")
    private BigDecimal premium;

    /**
     * 理论价值
     */
    @TableField("THEORETICAL_VALUE")
    private BigDecimal theoreticalValue;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 交易日期
     */
    @TableField("TRADE_DATE")
    private Date tradeDate;

    /**
     * 到期日期
     */
    @TableField("EXPIRY_DATE")
    private Date expiryDate;

    /**
     * 标的价格
     */
    @TableField("UNDERLYING_PRICE")
    private BigDecimal underlyingPrice;

    /**
     * 无风险利率(%)
     */
    @TableField("RISK_FREE_RATE")
    private BigDecimal riskFreeRate;

    /**
     * 波动率(%)
     */
    @TableField("VOLATILITY")
    private BigDecimal volatility;

    /**
     * 股息率(%)
     */
    @TableField("DIVIDEND_YIELD")
    private BigDecimal dividendYield;

    /**
     * 定价模型(BLACK_SCHOLES, BINOMIAL_TREE, MONTE_CARLO)
     */
    @TableField("PRICING_MODEL")
    private String pricingModel;

    /**
     * 行权方式(CASH_SETTLEMENT-现金交割, PHYSICAL_SETTLEMENT-实物交割)
     */
    @TableField("EXERCISE_TYPE")
    private String exerciseType;

    /**
     * Delta值
     */
    @TableField("DELTA")
    private BigDecimal delta;

    /**
     * Gamma值
     */
    @TableField("GAMMA")
    private BigDecimal gamma;

    /**
     * Theta值
     */
    @TableField("THETA")
    private BigDecimal theta;

    /**
     * Vega值
     */
    @TableField("VEGA")
    private BigDecimal vega;

    /**
     * Rho值
     */
    @TableField("RHO")
    private BigDecimal rho;

    /**
     * 交易对手
     */
    @TableField("COUNTERPARTY")
    private String counterparty;

    /**
     * 对手评级
     */
    @TableField("COUNTERPARTY_RATING")
    private String counterpartyRating;

    /**
     * 交易员
     */
    @TableField("TRADER")
    private String trader;

    /**
     * 销售员
     */
    @TableField("SALESPERSON")
    private String salesperson;

    /**
     * 状态(PENDING-待生效, ACTIVE-生效中, EXERCISED-已行权, EXPIRED-已到期, CANCELLED-已取消)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private Long updateBy;

    /**
     * 删除标志(0-正常, 1-删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 机构ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getOptionType() { return optionType; }
    public void setOptionType(String optionType) { this.optionType = optionType; }
    public String getOptionStyle() { return optionStyle; }
    public void setOptionStyle(String optionStyle) { this.optionStyle = optionStyle; }
    public String getUnderlyingAsset() { return underlyingAsset; }
    public void setUnderlyingAsset(String underlyingAsset) { this.underlyingAsset = underlyingAsset; }
    public BigDecimal getContractSize() { return contractSize; }
    public void setContractSize(BigDecimal contractSize) { this.contractSize = contractSize; }
    public BigDecimal getStrikePrice() { return strikePrice; }
    public void setStrikePrice(BigDecimal strikePrice) { this.strikePrice = strikePrice; }
    public BigDecimal getPremium() { return premium; }
    public void setPremium(BigDecimal premium) { this.premium = premium; }
    public BigDecimal getTheoreticalValue() { return theoreticalValue; }
    public void setTheoreticalValue(BigDecimal theoreticalValue) { this.theoreticalValue = theoreticalValue; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getTradeDate() { return tradeDate; }
    public void setTradeDate(Date tradeDate) { this.tradeDate = tradeDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public BigDecimal getUnderlyingPrice() { return underlyingPrice; }
    public void setUnderlyingPrice(BigDecimal underlyingPrice) { this.underlyingPrice = underlyingPrice; }
    public BigDecimal getRiskFreeRate() { return riskFreeRate; }
    public void setRiskFreeRate(BigDecimal riskFreeRate) { this.riskFreeRate = riskFreeRate; }
    public BigDecimal getVolatility() { return volatility; }
    public void setVolatility(BigDecimal volatility) { this.volatility = volatility; }
    public BigDecimal getDividendYield() { return dividendYield; }
    public void setDividendYield(BigDecimal dividendYield) { this.dividendYield = dividendYield; }
    public String getPricingModel() { return pricingModel; }
    public void setPricingModel(String pricingModel) { this.pricingModel = pricingModel; }
    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }
    public BigDecimal getDelta() { return delta; }
    public void setDelta(BigDecimal delta) { this.delta = delta; }
    public BigDecimal getGamma() { return gamma; }
    public void setGamma(BigDecimal gamma) { this.gamma = gamma; }
    public BigDecimal getTheta() { return theta; }
    public void setTheta(BigDecimal theta) { this.theta = theta; }
    public BigDecimal getVega() { return vega; }
    public void setVega(BigDecimal vega) { this.vega = vega; }
    public BigDecimal getRho() { return rho; }
    public void setRho(BigDecimal rho) { this.rho = rho; }
    public String getCounterparty() { return counterparty; }
    public void setCounterparty(String counterparty) { this.counterparty = counterparty; }
    public String getCounterpartyRating() { return counterpartyRating; }
    public void setCounterpartyRating(String counterpartyRating) { this.counterpartyRating = counterpartyRating; }
    public String getTrader() { return trader; }
    public void setTrader(String trader) { this.trader = trader; }
    public String getSalesperson() { return salesperson; }
    public void setSalesperson(String salesperson) { this.salesperson = salesperson; }
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
