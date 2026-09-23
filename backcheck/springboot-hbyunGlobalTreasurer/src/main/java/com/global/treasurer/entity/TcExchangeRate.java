package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财资公共模块 - 汇率数据表
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcExchangeRate", description = "汇率数据管理")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_EXCHANGE_RATE")
public class TcExchangeRate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 汇率日期
     */
    @TableField("RATE_DATE")
    @ApiModelProperty(value = "汇率日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rateDate;

    /**
     * 基准货币
     */
    @TableField("BASE_CURRENCY")
    @ApiModelProperty(value = "基准货币", required = true)
    private String baseCurrency;

    /**
     * 目标货币
     */
    @TableField("TARGET_CURRENCY")
    @ApiModelProperty(value = "目标货币", required = true)
    private String targetCurrency;

    /**
     * 汇率类型
     */
    @TableField("RATE_TYPE")
    @ApiModelProperty(value = "汇率类型", required = true)
    private String rateType;

    /**
     * 汇率
     */
    @TableField("EXCHANGE_RATE")
    @ApiModelProperty(value = "汇率", required = true)
    private BigDecimal exchangeRate;

    /**
     * 现汇买入价
     */
    @TableField("BUY_RATE")
    @ApiModelProperty(value = "现汇买入价")
    private BigDecimal buyRate;

    /**
     * 现汇卖出价
     */
    @TableField("SELL_RATE")
    @ApiModelProperty(value = "现汇卖出价")
    private BigDecimal sellRate;

    /**
     * 现钞买入价
     */
    @TableField("CASH_BUY_RATE")
    @ApiModelProperty(value = "现钞买入价")
    private BigDecimal cashBuyRate;

    /**
     * 现钞卖出价
     */
    @TableField("CASH_SELL_RATE")
    @ApiModelProperty(value = "现钞卖出价")
    private BigDecimal cashSellRate;

    /**
     * 中间价
     */
    @TableField("MIDDLE_RATE")
    @ApiModelProperty(value = "中间价")
    private BigDecimal middleRate;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    /**
     * 来源系统
     */
    @TableField("SOURCE_SYSTEM")
    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRE_TIME")
    @ApiModelProperty(value = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 是否交叉汇率：1-是，0-否
     */
    @TableField("IS_CROSS_RATE")
    @ApiModelProperty(value = "是否交叉汇率：1-是，0-否")
    private String isCrossRate;

    /**
     * 交叉换算路径
     */
    @TableField("CROSS_CALCULATION_PATH")
    @ApiModelProperty(value = "交叉换算路径")
    private String crossCalculationPath;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcExchangeRate ofId(String id) {
        TcExchangeRate exchangeRate = new TcExchangeRate();
        exchangeRate.setId(id);
        return exchangeRate;
    }

    /**
     * 根据货币对创建实例
     */
    public static TcExchangeRate ofCurrencyPair(String baseCurrency, String targetCurrency) {
        TcExchangeRate exchangeRate = new TcExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        return exchangeRate;
    }

    /**
     * 根据汇率日期和货币对创建实例
     */
    public static TcExchangeRate ofDateAndCurrencyPair(Date rateDate, String baseCurrency, String targetCurrency) {
        TcExchangeRate exchangeRate = new TcExchangeRate();
        exchangeRate.setRateDate(rateDate);
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        return exchangeRate;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getRateDate() { return rateDate; }
    public void setRateDate(Date rateDate) { this.rateDate = rateDate; }
    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public String getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }
    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public BigDecimal getBuyRate() { return buyRate; }
    public void setBuyRate(BigDecimal buyRate) { this.buyRate = buyRate; }
    public BigDecimal getSellRate() { return sellRate; }
    public void setSellRate(BigDecimal sellRate) { this.sellRate = sellRate; }
    public BigDecimal getCashBuyRate() { return cashBuyRate; }
    public void setCashBuyRate(BigDecimal cashBuyRate) { this.cashBuyRate = cashBuyRate; }
    public BigDecimal getCashSellRate() { return cashSellRate; }
    public void setCashSellRate(BigDecimal cashSellRate) { this.cashSellRate = cashSellRate; }
    public BigDecimal getMiddleRate() { return middleRate; }
    public void setMiddleRate(BigDecimal middleRate) { this.middleRate = middleRate; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
    public Date getEffectiveTime() { return effectiveTime; }
    public void setEffectiveTime(Date effectiveTime) { this.effectiveTime = effectiveTime; }
    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
    public String getIsCrossRate() { return isCrossRate; }
    public void setIsCrossRate(String isCrossRate) { this.isCrossRate = isCrossRate; }
    public String getCrossCalculationPath() { return crossCalculationPath; }
    public void setCrossCalculationPath(String crossCalculationPath) { this.crossCalculationPath = crossCalculationPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
