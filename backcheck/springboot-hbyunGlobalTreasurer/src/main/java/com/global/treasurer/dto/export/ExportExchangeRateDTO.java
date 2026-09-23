package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 汇率导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ExportExchangeRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "源币种", sort = 1, words = 10)
    private String fromCurrency;

    @ExcelField(title = "目标币种", sort = 2, words = 10)
    private String toCurrency;

    @ExcelField(title = "汇率", sort = 3, words = 15)
    private BigDecimal exchangeRate;

    @ExcelField(title = "中间价", sort = 4, words = 15)
    private BigDecimal middleRate;

    @ExcelField(title = "买入价", sort = 5, words = 15)
    private BigDecimal buyRate;

    @ExcelField(title = "卖出价", sort = 6, words = 15)
    private BigDecimal sellRate;

    @ExcelField(title = "汇率类型", sort = 7, words = 12)
    private String rateType;

    @ExcelField(title = "生效日期", sort = 8, words = 15)
    private String effectiveDate;

    @ExcelField(title = "汇率来源", sort = 9, words = 15)
    private String rateSource;

    @ExcelField(title = "状态", sort = 10, words = 10)
    private String status;

    @ExcelField(title = "创建时间", sort = 11, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportExchangeRateDTO fromEntity(com.global.treasurer.entity.TblGtExchangeRate entity) {
        if (entity == null) {
            return null;
        }
        ExportExchangeRateDTO dto = new ExportExchangeRateDTO();
        dto.setFromCurrency(entity.getFromCurrency());
        dto.setToCurrency(entity.getToCurrency());
        dto.setExchangeRate(entity.getExchangeRate());
        dto.setMiddleRate(entity.getMiddleRate());
        dto.setBuyRate(entity.getBuyRate());
        dto.setSellRate(entity.getSellRate());
        dto.setRateType(entity.getRateType());
        dto.setEffectiveDate(entity.getEffectiveDate() != null ? entity.getEffectiveDate().toString() : null);
        dto.setRateSource(entity.getRateSource());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().toString() : null);
        dto.setCreateTime(entity.getCreateTime());
        return dto;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题

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
    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getRateSource() { return rateSource; }
    public void setRateSource(String rateSource) { this.rateSource = rateSource; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
