package com.global.treasurer.dto.derivatives;

import com.global.treasurer.entity.derivatives.TblSwapTransaction;
import com.global.treasurer.util.excel.annotation.ExcelField;
import com.global.treasurer.util.excel.annotation.ExcelField.Align;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 掉期交易导出 DTO
 *
 * @author AI Developer
 * @date 2026-03-23
 */
public class SwapTransactionExportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "合约编号", sort = 1, words = 20, align = Align.CENTER)
    private String contractCode;

    @ExcelField(title = "掉期类型", sort = 2, words = 12, align = Align.CENTER)
    private String swapTypeText;

    @ExcelField(title = "名义本金", sort = 3, words = 18, align = Align.RIGHT, dataFormat = "#,##0.00")
    private BigDecimal notionalAmount;

    @ExcelField(title = "币种", sort = 4, words = 10, align = Align.CENTER)
    private String currency;

    @ExcelField(title = "固定利率(%)", sort = 5, words = 12, align = Align.RIGHT, dataFormat = "0.0000")
    private BigDecimal fixedRate;

    @ExcelField(title = "浮动利率基准", sort = 6, words = 15, align = Align.CENTER)
    private String floatingRateBasis;

    @ExcelField(title = "利差(bp)", sort = 7, words = 12, align = Align.RIGHT)
    private BigDecimal spread;

    @ExcelField(title = "支付频率", sort = 8, words = 12, align = Align.CENTER)
    private String paymentFrequency;

    @ExcelField(title = "生效日期", sort = 9, words = 15, align = Align.CENTER, dataFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    @ExcelField(title = "到期日期", sort = 10, words = 15, align = Align.CENTER, dataFormat = "yyyy-MM-dd")
    private Date maturityDate;

    @ExcelField(title = "交易对手", sort = 11, words = 18, align = Align.LEFT)
    private String counterparty;

    @ExcelField(title = "对手评级", sort = 12, words = 10, align = Align.CENTER)
    private String counterpartyRating;

    @ExcelField(title = "当前估值", sort = 13, words = 15, align = Align.RIGHT, dataFormat = "#,##0.00")
    private BigDecimal currentValue;

    @ExcelField(title = "下次支付日", sort = 14, words = 15, align = Align.CENTER, dataFormat = "yyyy-MM-dd")
    private Date nextPaymentDate;

    @ExcelField(title = "状态", sort = 15, words = 12, align = Align.CENTER)
    private String statusText;

    @ExcelField(title = "创建时间", sort = 16, words = 18, align = Align.CENTER, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelField(title = "备注", sort = 17, words = 30, align = Align.LEFT)
    private String remark;

    /**
     * 从实体转换为导出DTO
     */
    public static SwapTransactionExportDTO fromEntity(TblSwapTransaction transaction) {
        if (transaction == null) {
            return null;
        }
        SwapTransactionExportDTO dto = new SwapTransactionExportDTO();
        dto.setContractCode(transaction.getContractCode());
        dto.setSwapTypeText(convertSwapType(transaction.getSwapType()));
        dto.setNotionalAmount(transaction.getNotionalAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setFixedRate(transaction.getFixedRate());
        dto.setFloatingRateBasis(transaction.getFloatingRateBasis());
        dto.setSpread(transaction.getSpread());
        dto.setPaymentFrequency(convertPaymentFrequency(transaction.getPaymentFrequency()));
        dto.setEffectiveDate(transaction.getEffectiveDate());
        dto.setMaturityDate(transaction.getMaturityDate());
        dto.setCounterparty(transaction.getCounterparty());
        dto.setCounterpartyRating(transaction.getCounterpartyRating());
        dto.setCurrentValue(transaction.getCurrentValue());
        dto.setNextPaymentDate(transaction.getNextPaymentDate());
        dto.setStatusText(convertStatus(transaction.getStatus()));
        dto.setCreateTime(transaction.getCreateTime());
        dto.setRemark(transaction.getRemark());
        return dto;
    }

    private static String convertSwapType(String swapType) {
        if (swapType == null) return "";
        switch (swapType) {
            case "INTEREST_RATE": return "利率掉期";
            case "CURRENCY": return "货币掉期";
            case "COMMODITY": return "商品掉期";
            case "CDS": return "信用违约掉期";
            default: return swapType;
        }
    }

    private static String convertPaymentFrequency(String frequency) {
        if (frequency == null) return "";
        switch (frequency) {
            case "MONTHLY": return "月度";
            case "QUARTERLY": return "季度";
            case "SEMI_ANNUAL": return "半年";
            case "ANNUAL": return "年度";
            default: return frequency;
        }
    }

    private static String convertStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待生效";
            case "ACTIVE": return "生效中";
            case "TERMINATED": return "已终止";
            case "MATURED": return "已到期";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }

    // Getters and Setters
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getSwapTypeText() {
        return swapTypeText;
    }

    public void setSwapTypeText(String swapTypeText) {
        this.swapTypeText = swapTypeText;
    }

    public BigDecimal getNotionalAmount() {
        return notionalAmount;
    }

    public void setNotionalAmount(BigDecimal notionalAmount) {
        this.notionalAmount = notionalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public String getFloatingRateBasis() {
        return floatingRateBasis;
    }

    public void setFloatingRateBasis(String floatingRateBasis) {
        this.floatingRateBasis = floatingRateBasis;
    }

    public BigDecimal getSpread() {
        return spread;
    }

    public void setSpread(BigDecimal spread) {
        this.spread = spread;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getCounterpartyRating() {
        return counterpartyRating;
    }

    public void setCounterpartyRating(String counterpartyRating) {
        this.counterpartyRating = counterpartyRating;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public Date getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(Date nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
