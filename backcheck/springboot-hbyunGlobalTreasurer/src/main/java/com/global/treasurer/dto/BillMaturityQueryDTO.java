package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据到期查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillMaturityQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 票据编号 */
    private String billNumber;

    /** 票据类型 */
    private String billType;

    /** 出票人名称 */
    private String drawerName;

    /** 承兑人名称 */
    private String acceptorName;

    /** 到期状态 */
    private String maturityStatus;

    /** 提醒状态 */
    private String reminderStatus;

    /** 处置状态 */
    private String processStatus;

    /** 到期日期开始 */
    private Date maturityDateStart;

    /** 到期日期结束 */
    private Date maturityDateEnd;

    /** 剩余天数下限 */
    private Integer remainingDaysMin;

    /** 剩余天数上限 */
    private Integer remainingDaysMax;

    /** 金额下限 */
    private BigDecimal amountMin;

    /** 金额上限 */
    private BigDecimal amountMax;

    /** 公司ID */
    private Long companyId;

    /** 部门ID */
    private Long deptId;

    /** 当前页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public String getMaturityStatus() { return maturityStatus; }
    public void setMaturityStatus(String maturityStatus) { this.maturityStatus = maturityStatus; }
    public String getReminderStatus() { return reminderStatus; }
    public void setReminderStatus(String reminderStatus) { this.reminderStatus = reminderStatus; }
    public String getProcessStatus() { return processStatus; }
    public void setProcessStatus(String processStatus) { this.processStatus = processStatus; }
    public Date getMaturityDateStart() { return maturityDateStart; }
    public void setMaturityDateStart(Date maturityDateStart) { this.maturityDateStart = maturityDateStart; }
    public Date getMaturityDateEnd() { return maturityDateEnd; }
    public void setMaturityDateEnd(Date maturityDateEnd) { this.maturityDateEnd = maturityDateEnd; }
    public Integer getRemainingDaysMin() { return remainingDaysMin; }
    public void setRemainingDaysMin(Integer remainingDaysMin) { this.remainingDaysMin = remainingDaysMin; }
    public Integer getRemainingDaysMax() { return remainingDaysMax; }
    public void setRemainingDaysMax(Integer remainingDaysMax) { this.remainingDaysMax = remainingDaysMax; }
    public BigDecimal getAmountMin() { return amountMin; }
    public void setAmountMin(BigDecimal amountMin) { this.amountMin = amountMin; }
    public BigDecimal getAmountMax() { return amountMax; }
    public void setAmountMax(BigDecimal amountMax) { this.amountMax = amountMax; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
