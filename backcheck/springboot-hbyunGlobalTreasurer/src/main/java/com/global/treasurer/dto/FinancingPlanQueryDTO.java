package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 融资计划查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingPlanQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 计划年度 */
    private Integer planYear;

    /** 计划类型 */
    private String planType;

    /** 公司ID */
    private Long companyId;

    /** 计划状态 */
    private String planStatus;

    /** 币种 */
    private String currencyCode;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Integer getPlanYear() { return planYear; }
    public void setPlanYear(Integer planYear) { this.planYear = planYear; }
    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
