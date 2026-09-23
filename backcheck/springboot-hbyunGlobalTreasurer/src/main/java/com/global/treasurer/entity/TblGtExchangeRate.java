package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 汇率配置实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GT_EXCHANGE_RATE")
public class TblGtExchangeRate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 汇率ID
     */
    @TableId(value = "RATE_ID", type = IdType.AUTO)
    private Long rateId;

    /**
     * 源币种
     */
    private String fromCurrency;

    /**
     * 目标币种
     */
    private String toCurrency;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 中间价
     */
    private BigDecimal middleRate;

    /**
     * 买入价
     */
    private BigDecimal buyRate;

    /**
     * 卖出价
     */
    private BigDecimal sellRate;

    /**
     * 现钞买入价
     */
    private BigDecimal cashBuyRate;

    /**
     * 现钞卖出价
     */
    private BigDecimal cashSellRate;

    /**
     * 汇率类型
     */
    private String rateType;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 失效日期
     */
    private LocalDate expireDate;

    /**
     * 汇率来源
     */
    private String rateSource;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 是否启用
     */
    private Integer isActive;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getRateId() { return rateId; }
    public void setRateId(Long rateId) { this.rateId = rateId; }

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }

    public BigDecimal getMiddleRate() { return middleRate; }
    public void setMiddleRate(BigDecimal middleRate) { this.middleRate = middleRate; }

    public BigDecimal getBuyRate() { return buyRate; }
    public void setBuyRate(BigDecimal buyRate) { this.buyRate = buyRate; }

    public BigDecimal getSellRate() { return sellRate; }
    public void setSellRate(BigDecimal sellRate) { this.sellRate = sellRate; }

    public BigDecimal getCashBuyRate() { return cashBuyRate; }
    public void setCashBuyRate(BigDecimal cashBuyRate) { this.cashBuyRate = cashBuyRate; }

    public BigDecimal getCashSellRate() { return cashSellRate; }
    public void setCashSellRate(BigDecimal cashSellRate) { this.cashSellRate = cashSellRate; }

    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; }

    public String getRateSource() { return rateSource; }
    public void setRateSource(String rateSource) { this.rateSource = rateSource; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
