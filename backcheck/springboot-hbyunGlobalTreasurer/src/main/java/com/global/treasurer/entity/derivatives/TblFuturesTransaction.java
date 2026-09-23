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
 * 期货交易实体类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUTURES_TRANSACTION")
public class TblFuturesTransaction implements Serializable {
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
     * 期货类型(COMMODITY-商品期货, FINANCIAL-金融期货, INDEX-股指期货, FX-外汇期货)
     */
    @TableField(value = "FUTURES_TYPE")
    private String futuresType;

    /**
     * 标的资产
     */
    @TableField(value = "UNDERLYING_ASSET")
    private String underlyingAsset;

    /**
     * 交易方向(LONG-多头, SHORT-空头)
     */
    @TableField(value = "DIRECTION")
    private String direction;

    /**
     * 合约数量
     */
    @TableField(value = "CONTRACT_SIZE")
    private BigDecimal contractSize;

    /**
     * 开仓价格
     */
    @TableField(value = "OPEN_PRICE")
    private BigDecimal openPrice;

    /**
     * 当前价格
     */
    @TableField(value = "CURRENT_PRICE")
    private BigDecimal currentPrice;

    /**
     * 币种
     */
    @TableField(value = "CURRENCY")
    private String currency;

    /**
     * 交易日期
     */
    @TableField(value = "TRADE_DATE")
    private Date tradeDate;

    /**
     * 到期日期
     */
    @TableField(value = "EXPIRY_DATE")
    private Date expiryDate;

    /**
     * 初始保证金
     */
    @TableField(value = "INITIAL_MARGIN")
    private BigDecimal initialMargin;

    /**
     * 维持保证金
     */
    @TableField(value = "MAINTENANCE_MARGIN")
    private BigDecimal maintenanceMargin;

    /**
     * 保证金比例(%)
     */
    @TableField(value = "MARGIN_RATIO")
    private BigDecimal marginRatio;

    /**
     * 保证金余额
     */
    @TableField(value = "MARGIN")
    private BigDecimal margin;

    /**
     * 浮动盈亏
     */
    @TableField(value = "UNREALIZED_PNL")
    private BigDecimal unrealizedPnl;

    /**
     * 已实现盈亏
     */
    @TableField(value = "REALIZED_PNL")
    private BigDecimal realizedPnl;

    /**
     * 交易所(SHFE-上期所, DCE-大商所, CZCE-郑商所, CFFEX-中金所, INE-能源中心)
     * EXCHANGE是达梦数据库保留字,需要用双引号包裹
     */
    @TableField(value = "\"EXCHANGE\"")
    private String exchange;

    /**
     * 期货公司
     */
    @TableField(value = "BROKER")
    private String broker;

    /**
     * 交易员
     */
    @TableField(value = "TRADER")
    private String trader;

    /**
     * 状态(PENDING-待生效, OPEN-持仓中, CLOSED-已平仓, SETTLED-已交割, CANCELLED-已取消)
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
     * 删除标志(0-正常, 1-删除)
     */
    @TableField(value = "DEL_FLAG")
    private String delFlag;

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
    public String getFuturesType() { return futuresType; }
    public void setFuturesType(String futuresType) { this.futuresType = futuresType; }
    public String getUnderlyingAsset() { return underlyingAsset; }
    public void setUnderlyingAsset(String underlyingAsset) { this.underlyingAsset = underlyingAsset; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public BigDecimal getContractSize() { return contractSize; }
    public void setContractSize(BigDecimal contractSize) { this.contractSize = contractSize; }
    public BigDecimal getOpenPrice() { return openPrice; }
    public void setOpenPrice(BigDecimal openPrice) { this.openPrice = openPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getTradeDate() { return tradeDate; }
    public void setTradeDate(Date tradeDate) { this.tradeDate = tradeDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public BigDecimal getInitialMargin() { return initialMargin; }
    public void setInitialMargin(BigDecimal initialMargin) { this.initialMargin = initialMargin; }
    public BigDecimal getMaintenanceMargin() { return maintenanceMargin; }
    public void setMaintenanceMargin(BigDecimal maintenanceMargin) { this.maintenanceMargin = maintenanceMargin; }
    public BigDecimal getMarginRatio() { return marginRatio; }
    public void setMarginRatio(BigDecimal marginRatio) { this.marginRatio = marginRatio; }
    public BigDecimal getMargin() { return margin; }
    public void setMargin(BigDecimal margin) { this.margin = margin; }
    public BigDecimal getUnrealizedPnl() { return unrealizedPnl; }
    public void setUnrealizedPnl(BigDecimal unrealizedPnl) { this.unrealizedPnl = unrealizedPnl; }
    public BigDecimal getRealizedPnl() { return realizedPnl; }
    public void setRealizedPnl(BigDecimal realizedPnl) { this.realizedPnl = realizedPnl; }
    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }
    public String getBroker() { return broker; }
    public void setBroker(String broker) { this.broker = broker; }
    public String getTrader() { return trader; }
    public void setTrader(String trader) { this.trader = trader; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
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
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}
