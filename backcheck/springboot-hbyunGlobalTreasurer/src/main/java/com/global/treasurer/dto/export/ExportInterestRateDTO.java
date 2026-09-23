package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 利率导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ExportInterestRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "利率类型", sort = 1, words = 15)
    private String rateType;

    @ExcelField(title = "币种代码", sort = 2, words = 10)
    private String currencyCode;

    @ExcelField(title = "期限", sort = 3, words = 10)
    private String term;

    @ExcelField(title = "利率值(%)", sort = 4, words = 12)
    private BigDecimal interestRate;

    @ExcelField(title = "利率日期", sort = 5, words = 15)
    private String rateDate;

    @ExcelField(title = "利率来源", sort = 6, words = 15)
    private String rateSource;

    @ExcelField(title = "状态", sort = 7, words = 10)
    private String status;

    @ExcelField(title = "备注", sort = 8, words = 30)
    private String remark;

    @ExcelField(title = "创建时间", sort = 9, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportInterestRateDTO fromEntity(com.global.treasurer.entity.TblGtInterestRate entity) {
        if (entity == null) {
            return null;
        }
        ExportInterestRateDTO dto = new ExportInterestRateDTO();
        dto.setRateType(convertRateType(entity.getRateType()));
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setTerm(entity.getTerm() + " " + convertTermUnit(entity.getTermUnit()));
        dto.setInterestRate(entity.getInterestRate());
        dto.setRateDate(entity.getRateDate() != null ? entity.getRateDate().toString() : "");
        dto.setRateSource(entity.getRateSource());
        dto.setStatus(entity.getStatus() != null && entity.getStatus() == 1 ? "启用" : "停用");
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        return dto;
    }

    private static String convertRateType(String type) {
        if (type == null) return "";
        switch (type) {
            case "LPR": return "贷款市场报价利率";
            case "SHIBOR": return "上海银行间同业拆放利率";
            case "DEPOSIT": return "存款利率";
            case "LOAN": return "贷款利率";
            default: return type;
        }
    }

    private static String convertTermUnit(String unit) {
        if (unit == null) return "";
        switch (unit) {
            case "D": return "天";
            case "M": return "月";
            case "Y": return "年";
            default: return unit;
        }
    }


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getRateDate() { return rateDate; }
    public void setRateDate(String rateDate) { this.rateDate = rateDate; }
    public String getRateSource() { return rateSource; }
    public void setRateSource(String rateSource) { this.rateSource = rateSource; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}
