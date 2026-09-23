package com.global.treasurer.dto;

// import lombok.Data; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.util.Date;

/**
 * 融资审批查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingApprovalQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNo = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 审批ID */
    private Long approvalId;

    /** 融资类型 */
    private String financingType;

    /** 融资编号 */
    private String financingNo;

    /** 公司ID */
    private Long companyId;

    /** 申请人ID */
    private Long applicantId;

    /** 审批状态 */
    private String approvalStatus;

    /** 当前审批节点 */
    private String currentApprovalNode;

    /** 申请时间-开始 */
    private Date applicationTimeStart;

    /** 申请时间-结束 */
    private Date applicationTimeEnd;

    /** 关键字搜索(融资编号、公司名称) */
    private String keyword;

    // 显式添加getter方法以确保编译通过
    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public String getFinancingType() {
        return financingType;
    }

    public String getFinancingNo() {
        return financingNo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getCurrentApprovalNode() {
        return currentApprovalNode;
    }

    public Date getApplicationTimeStart() {
        return applicationTimeStart;
    }

    public Date getApplicationTimeEnd() {
        return applicationTimeEnd;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public void setFinancingType(String financingType) {
        this.financingType = financingType;
    }

    public void setFinancingNo(String financingNo) {
        this.financingNo = financingNo;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void setCurrentApprovalNode(String currentApprovalNode) {
        this.currentApprovalNode = currentApprovalNode;
    }

    public void setApplicationTimeStart(Date applicationTimeStart) {
        this.applicationTimeStart = applicationTimeStart;
    }

    public void setApplicationTimeEnd(Date applicationTimeEnd) {
        this.applicationTimeEnd = applicationTimeEnd;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
