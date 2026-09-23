package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
import com.global.treasurer.entity.TblGtAccountFreeze;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户冻结记录导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-03-11
 */
public class ExportAccountFreezeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "记录ID", sort = 1, words = 15)
    private String recordId;

    @ExcelField(title = "账户号码", sort = 2, words = 22)
    private String accountNumber;

    @ExcelField(title = "账户名称", sort = 3, words = 20)
    private String accountName;

    @ExcelField(title = "币种", sort = 4, words = 8)
    private String currencyCode;

    @ExcelField(title = "冻结类型", sort = 5, words = 12)
    private String freezeType;

    @ExcelField(title = "冻结金额", sort = 6, words = 15)
    private BigDecimal freezeAmount;

    @ExcelField(title = "已解冻金额", sort = 7, words = 15)
    private BigDecimal unfreezeAmount;

    @ExcelField(title = "冻结状态", sort = 8, words = 12)
    private String freezeStatus;

    @ExcelField(title = "冻结原因", sort = 9, words = 25)
    private String freezeReason;

    @ExcelField(title = "冻结日期", sort = 10, words = 14)
    private String freezeDate;

    @ExcelField(title = "解冻日期", sort = 11, words = 14)
    private String unfreezeDate;

    @ExcelField(title = "解冻原因", sort = 12, words = 25)
    private String unfreezeReason;

    @ExcelField(title = "执行机构", sort = 13, words = 15)
    private String executionOrg;

    @ExcelField(title = "文书号", sort = 14, words = 18)
    private String documentNo;

    @ExcelField(title = "备注", sort = 15, words = 25)
    private String remark;

    @ExcelField(title = "创建时间", sort = 16, words = 20)
    private String createTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportAccountFreezeDTO fromEntity(TblGtAccountFreeze entity) {
        if (entity == null) return null;
        ExportAccountFreezeDTO dto = new ExportAccountFreezeDTO();
        dto.setRecordId(entity.getRecordId() != null ? entity.getRecordId().toString() : null);
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setAccountName(entity.getAccountName());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setFreezeType(entity.getFreezeType());
        dto.setFreezeAmount(entity.getFreezeAmount());
        dto.setUnfreezeAmount(entity.getUnfreezeAmount());
        dto.setFreezeStatus(entity.getFreezeStatus());
        dto.setFreezeReason(entity.getFreezeReason());
        dto.setFreezeDate(entity.getFreezeDate() != null ? entity.getFreezeDate().toString() : null);
        dto.setUnfreezeDate(entity.getUnfreezeDate() != null ? entity.getUnfreezeDate().toString() : null);
        dto.setUnfreezeReason(entity.getUnfreezeReason());
        dto.setExecutionOrg(entity.getExecutionOrg());
        dto.setDocumentNo(entity.getDocumentNo());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime() != null ? entity.getCreateTime().toString() : null);
        return dto;
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getFreezeType() { return freezeType; }
    public void setFreezeType(String freezeType) { this.freezeType = freezeType; }
    public BigDecimal getFreezeAmount() { return freezeAmount; }
    public void setFreezeAmount(BigDecimal freezeAmount) { this.freezeAmount = freezeAmount; }
    public BigDecimal getUnfreezeAmount() { return unfreezeAmount; }
    public void setUnfreezeAmount(BigDecimal unfreezeAmount) { this.unfreezeAmount = unfreezeAmount; }
    public String getFreezeStatus() { return freezeStatus; }
    public void setFreezeStatus(String freezeStatus) { this.freezeStatus = freezeStatus; }
    public String getFreezeReason() { return freezeReason; }
    public void setFreezeReason(String freezeReason) { this.freezeReason = freezeReason; }
    public String getFreezeDate() { return freezeDate; }
    public void setFreezeDate(String freezeDate) { this.freezeDate = freezeDate; }
    public String getUnfreezeDate() { return unfreezeDate; }
    public void setUnfreezeDate(String unfreezeDate) { this.unfreezeDate = unfreezeDate; }
    public String getUnfreezeReason() { return unfreezeReason; }
    public void setUnfreezeReason(String unfreezeReason) { this.unfreezeReason = unfreezeReason; }
    public String getExecutionOrg() { return executionOrg; }
    public void setExecutionOrg(String executionOrg) { this.executionOrg = executionOrg; }
    public String getDocumentNo() { return documentNo; }
    public void setDocumentNo(String documentNo) { this.documentNo = documentNo; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}

