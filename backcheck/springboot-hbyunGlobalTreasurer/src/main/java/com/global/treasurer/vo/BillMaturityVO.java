package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据到期VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillMaturityVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long maturityId;
    private Long billId;
    private String billNumber;
    private String billType;
    private String billTypeName;
    private BigDecimal billAmount;
    private String drawerName;
    private String acceptorName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;

    private Integer remainingDays;
    private String maturityStatus;
    private String maturityStatusName;
    private String reminderStatus;
    private String reminderStatusName;
    private String processType;
    private String processTypeName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date processDate;

    private String processDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String createBy;
    private String updateBy;
    private Long companyId;
    private String companyName;
    private Long deptId;
    private String deptName;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getMaturityId() { return maturityId; }
    public void setMaturityId(Long maturityId) { this.maturityId = maturityId; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillTypeName() { return billTypeName; }
    public void setBillTypeName(String billTypeName) { this.billTypeName = billTypeName; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public Integer getRemainingDays() { return remainingDays; }
    public void setRemainingDays(Integer remainingDays) { this.remainingDays = remainingDays; }
    public String getMaturityStatus() { return maturityStatus; }
    public void setMaturityStatus(String maturityStatus) { this.maturityStatus = maturityStatus; }
    public String getMaturityStatusName() { return maturityStatusName; }
    public void setMaturityStatusName(String maturityStatusName) { this.maturityStatusName = maturityStatusName; }
    public String getReminderStatus() { return reminderStatus; }
    public void setReminderStatus(String reminderStatus) { this.reminderStatus = reminderStatus; }
    public String getReminderStatusName() { return reminderStatusName; }
    public void setReminderStatusName(String reminderStatusName) { this.reminderStatusName = reminderStatusName; }
    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }
    public String getProcessTypeName() { return processTypeName; }
    public void setProcessTypeName(String processTypeName) { this.processTypeName = processTypeName; }
    public Date getProcessDate() { return processDate; }
    public void setProcessDate(Date processDate) { this.processDate = processDate; }
    public String getProcessDescription() { return processDescription; }
    public void setProcessDescription(String processDescription) { this.processDescription = processDescription; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

}
