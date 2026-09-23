package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 融资还款查询DTO
 *
 * @author HuaBo Cloud
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingRepaymentQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer page = 1;

    /** 每页数量 */
    private Integer limit = 10;

    /** 融资编号 */
    private String financingNo;

    /** 融资类型 */
    private Integer financingType;

    /** 金融机构 */
    private String financialInstitution;

    /** 还款状态(0-未还款 1-部分还款 2-已还款 3-逾期未还 4-逾期已还) */
    private Integer repaymentStatus;

    /** 还款方式 */
    private Integer repaymentMethod;

    /** 计划还款日期开始 */
    private String plannedRepaymentDateStart;

    /** 计划还款日期结束 */
    private String plannedRepaymentDateEnd;

    /** 实际还款日期开始 */
    private String actualRepaymentDateStart;

    /** 实际还款日期结束 */
    private String actualRepaymentDateEnd;

    /** 币种 */
    private String currency;

    /** 是否逾期(0-否 1-是) */
    private Integer isOverdue;

    /** 经办人 */
    private String operator;

    /** 审核人 */
    private String reviewer;

    /** 还款状态列表 */
    private java.util.List<Integer> repaymentStatusList;

    /** 天数 */
    private Integer days;

    /** 展期次数 */
    private Integer extensionCount;

    /** 展期原因 */
    private String extensionReason;

    /** 剩余天数 */
    private Integer remainingDays;

    /** 还款类型 */
    private Integer repaymentType;

    /** 公司ID */
    private Long companyId;

    /** 融资ID */
    private Long financingId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getFinancingNo() { return financingNo; }
    public void setFinancingNo(String financingNo) { this.financingNo = financingNo; }
    public Integer getFinancingType() { return financingType; }
    public void setFinancingType(Integer financingType) { this.financingType = financingType; }
    public String getFinancialInstitution() { return financialInstitution; }
    public void setFinancialInstitution(String financialInstitution) { this.financialInstitution = financialInstitution; }
    public Integer getRepaymentStatus() { return repaymentStatus; }
    public void setRepaymentStatus(Integer repaymentStatus) { this.repaymentStatus = repaymentStatus; }
    public Integer getRepaymentMethod() { return repaymentMethod; }
    public void setRepaymentMethod(Integer repaymentMethod) { this.repaymentMethod = repaymentMethod; }
    public String getPlannedRepaymentDateStart() { return plannedRepaymentDateStart; }
    public void setPlannedRepaymentDateStart(String plannedRepaymentDateStart) { this.plannedRepaymentDateStart = plannedRepaymentDateStart; }
    public String getPlannedRepaymentDateEnd() { return plannedRepaymentDateEnd; }
    public void setPlannedRepaymentDateEnd(String plannedRepaymentDateEnd) { this.plannedRepaymentDateEnd = plannedRepaymentDateEnd; }
    public String getActualRepaymentDateStart() { return actualRepaymentDateStart; }
    public void setActualRepaymentDateStart(String actualRepaymentDateStart) { this.actualRepaymentDateStart = actualRepaymentDateStart; }
    public String getActualRepaymentDateEnd() { return actualRepaymentDateEnd; }
    public void setActualRepaymentDateEnd(String actualRepaymentDateEnd) { this.actualRepaymentDateEnd = actualRepaymentDateEnd; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Integer getIsOverdue() { return isOverdue; }
    public void setIsOverdue(Integer isOverdue) { this.isOverdue = isOverdue; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }
    public java.util.List<Integer> getRepaymentStatusList() { return repaymentStatusList; }
    public void setRepaymentStatusList(java.util.List<Integer> repaymentStatusList) { this.repaymentStatusList = repaymentStatusList; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public Integer getExtensionCount() { return extensionCount; }
    public void setExtensionCount(Integer extensionCount) { this.extensionCount = extensionCount; }
    public String getExtensionReason() { return extensionReason; }
    public void setExtensionReason(String extensionReason) { this.extensionReason = extensionReason; }
    public Integer getRemainingDays() { return remainingDays; }
    public void setRemainingDays(Integer remainingDays) { this.remainingDays = remainingDays; }
    public Integer getRepaymentType() { return repaymentType; }
    public void setRepaymentType(Integer repaymentType) { this.repaymentType = repaymentType; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getFinancingId() { return financingId; }
    public void setFinancingId(Long financingId) { this.financingId = financingId; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public Integer getPageNum() { return page; }
    public void setPageNum(Integer pageNum) { this.page = pageNum; }
    public Integer getPageSize() { return limit; }
    public void setPageSize(Integer pageSize) { this.limit = pageSize; }

}
