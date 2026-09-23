package com.global.treasurer.dto.export;

import com.global.treasurer.entity.TblFinancialLease;
import com.global.treasurer.util.excel.annotation.ExcelField;
import com.global.treasurer.util.excel.fieldtype.MoneyType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 融资租赁导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public class ExportFinancialLeaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "租赁编号", sort = 1, words = 18)
    private String leaseNo;

    @ExcelField(title = "租赁类型", sort = 2, words = 12)
    private String leasingType;

    @ExcelField(title = "租赁公司", sort = 3, words = 20)
    private String leasingCompany;

    @ExcelField(title = "资产类型", sort = 4, words = 12)
    private String assetType;

    @ExcelField(title = "资产描述", sort = 5, words = 25)
    private String assetDescription;

    @ExcelField(title = "资产价值(元)", sort = 6, words = 15, fieldType = MoneyType.class)
    private BigDecimal assetValue;

    @ExcelField(title = "租赁金额(元)", sort = 7, words = 15, fieldType = MoneyType.class)
    private BigDecimal leasingAmount;

    @ExcelField(title = "币种", sort = 8, words = 8)
    private String currencyCode;

    @ExcelField(title = "租赁期限", sort = 9, words = 10)
    private String leasingTerm;

    @ExcelField(title = "利率(%)", sort = 10, words = 10, dataFormat = "0.00")
    private BigDecimal interestRate;

    @ExcelField(title = "月租金(元)", sort = 11, words = 15, fieldType = MoneyType.class)
    private BigDecimal monthlyRent;

    @ExcelField(title = "残值(元)", sort = 12, words = 12, fieldType = MoneyType.class)
    private BigDecimal residualValue;

    @ExcelField(title = "保证金(元)", sort = 13, words = 12, fieldType = MoneyType.class)
    private BigDecimal securityDeposit;

    @ExcelField(title = "申请状态", sort = 14, words = 10)
    private String applicationStatus;

    @ExcelField(title = "申请日期", sort = 15, words = 12, dataFormat = "yyyy-MM-dd")
    private Date applicationDate;

    @ExcelField(title = "合同编号", sort = 16, words = 18)
    private String contractNo;

    @ExcelField(title = "起租日期", sort = 17, words = 12, dataFormat = "yyyy-MM-dd")
    private Date startDate;

    @ExcelField(title = "到期日期", sort = 18, words = 12, dataFormat = "yyyy-MM-dd")
    private Date endDate;

    @ExcelField(title = "所属公司", sort = 19, words = 20)
    private String companyName;

    @ExcelField(title = "备注", sort = 20, words = 30)
    private String remark;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportFinancialLeaseDTO fromEntity(TblFinancialLease entity) {
        if (entity == null) {
            return null;
        }
        ExportFinancialLeaseDTO dto = new ExportFinancialLeaseDTO();
        dto.setLeaseNo(entity.getLeaseNo());
        dto.setLeasingType(convertLeasingType(entity.getLeasingType()));
        dto.setLeasingCompany(entity.getLeasingCompany());
        // 资产相关字段 - 暂时使用空值，这些字段存储在TblLeaseAsset关联表中
        dto.setAssetType("");  // entity.getAssetType()
        dto.setAssetDescription("");  // entity.getAssetDescription()
        dto.setAssetValue(null);  // entity.getAssetValue()
        dto.setLeasingAmount(entity.getLeasingAmount());
        dto.setCurrencyCode(convertCurrency(entity.getCurrencyCode()));
        // 租赁条款字段 - 暂时使用空值，这些字段需要从关联表获取
        dto.setLeasingTerm("");  // formatTerm(entity.getLeasingTerm(), entity.getTermUnit())
        dto.setInterestRate(entity.getInterestRate());
        dto.setMonthlyRent(null);  // entity.getMonthlyRent()
        dto.setResidualValue(null);  // entity.getResidualValue()
        dto.setSecurityDeposit(null);  // entity.getSecurityDeposit()
        dto.setApplicationStatus(convertStatus(entity.getApplicationStatus()));
        dto.setApplicationDate(null);  // entity.getApplicationDate()
        dto.setContractNo("");  // entity.getContractNo()
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setCompanyName(entity.getCompanyName());
        dto.setRemark("");  // entity.getRemark()
        return dto;
    }

    private static String convertLeasingType(String type) {
        if (type == null) return "";
        switch (type) {
            case "DIRECT": return "直接租赁";
            case "LEASEBACK": return "售后回租";
            case "LEVERAGED": return "杠杆租赁";
            case "OPERATING": return "经营租赁";
            default: return type;
        }
    }

    private static String convertCurrency(String code) {
        if (code == null) return "";
        switch (code) {
            case "CNY": return "人民币";
            case "USD": return "美元";
            case "EUR": return "欧元";
            case "HKD": return "港币";
            default: return code;
        }
    }

    private static String formatTerm(Integer term, String unit) {
        if (term == null) return "";
        String unitStr = "";
        if (unit != null) {
            switch (unit) {
                case "M": unitStr = "个月"; break;
                case "Y": unitStr = "年"; break;
                case "D": unitStr = "天"; break;
                default: unitStr = unit;
            }
        }
        return term + unitStr;
    }

    private static String convertStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待提交";
            case "SUBMITTED": return "已提交";
            case "APPROVED": return "已审批";
            case "REJECTED": return "已拒绝";
            case "ACTIVE": return "执行中";
            case "COMPLETED": return "已完成";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }

    // Getter/Setter methods
    public String getLeaseNo() { return leaseNo; }
    public void setLeaseNo(String leaseNo) { this.leaseNo = leaseNo; }
    public String getLeasingType() { return leasingType; }
    public void setLeasingType(String leasingType) { this.leasingType = leasingType; }
    public String getLeasingCompany() { return leasingCompany; }
    public void setLeasingCompany(String leasingCompany) { this.leasingCompany = leasingCompany; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }
    public String getAssetDescription() { return assetDescription; }
    public void setAssetDescription(String assetDescription) { this.assetDescription = assetDescription; }
    public BigDecimal getAssetValue() { return assetValue; }
    public void setAssetValue(BigDecimal assetValue) { this.assetValue = assetValue; }
    public BigDecimal getLeasingAmount() { return leasingAmount; }
    public void setLeasingAmount(BigDecimal leasingAmount) { this.leasingAmount = leasingAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getLeasingTerm() { return leasingTerm; }
    public void setLeasingTerm(String leasingTerm) { this.leasingTerm = leasingTerm; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }
    public BigDecimal getResidualValue() { return residualValue; }
    public void setResidualValue(BigDecimal residualValue) { this.residualValue = residualValue; }
    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { this.securityDeposit = securityDeposit; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

