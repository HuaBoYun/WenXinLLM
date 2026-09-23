package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 融资租赁查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancialLeaseQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 租赁类型 */
    private String leasingType;

    /** 申请状态 */
    private String applicationStatus;

    /** 公司ID */
    private Long companyId;

    /** 币种 */
    private String currencyCode;

    /** 租赁公司 */
    private String leasingCompany;

    /** 租赁编号 */
    private String leaseNo;


    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public String getLeasingType() { return leasingType; }
    public void setLeasingType(String leasingType) { this.leasingType = leasingType; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getLeasingCompany() { return leasingCompany; }
    public void setLeasingCompany(String leasingCompany) { this.leasingCompany = leasingCompany; }
    public String getLeaseNo() { return leaseNo; }
    public void setLeaseNo(String leaseNo) { this.leaseNo = leaseNo; }
}
