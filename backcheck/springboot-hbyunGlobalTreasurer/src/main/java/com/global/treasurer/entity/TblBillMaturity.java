package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据到期实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_MATURITY")
public class TblBillMaturity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MATURITY_ID", type = IdType.INPUT)
    private Long maturityId;

    @TableField("BILL_ID")
    private Long billId;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("BILL_TYPE")
    private String billType;

    @TableField("BILL_AMOUNT")
    private BigDecimal billAmount;

    @TableField("DRAWER_NAME")
    private String drawerName;

    @TableField("ACCEPTOR_NAME")
    private String acceptorName;

    @TableField("ISSUE_DATE")
    private Date issueDate;

    @TableField("MATURITY_DATE")
    private Date maturityDate;

    @TableField("REMAINING_DAYS")
    private Integer remainingDays;

    @TableField("MATURITY_STATUS")
    private String maturityStatus;

    @TableField("REMINDER_STATUS")
    private String reminderStatus;

    @TableField("PROCESS_TYPE")
    private String processType;

    @TableField("PROCESS_DATE")
    private Date processDate;

    @TableField("PROCESS_DESCRIPTION")
    private String processDescription;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getMaturityId() { return maturityId; }
    public void setMaturityId(Long maturityId) { this.maturityId = maturityId; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
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
    public String getReminderStatus() { return reminderStatus; }
    public void setReminderStatus(String reminderStatus) { this.reminderStatus = reminderStatus; }
    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }
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
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
}
