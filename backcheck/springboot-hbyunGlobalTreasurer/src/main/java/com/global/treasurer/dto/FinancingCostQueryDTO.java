package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 融资成本查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingCostQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 融资ID */
    private Long financingId;

    /** 融资类型 */
    private String financingType;

    /** 币种 */
    private String currencyCode;

    /** 公司ID */
    private Long companyId;

    /** 周期类型 */
    private String periodType;

    /** 周期值 */
    private String periodValue;

    /** 开始日期 */
    private Date startDate;

    /** 结束日期 */
    private Date endDate;

    /** 最小成本率 */
    private Double minCostRate;

    /** 最大成本率 */
    private Double maxCostRate;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getFinancingId() { return financingId; }
    public void setFinancingId(Long financingId) { this.financingId = financingId; }
    public String getFinancingType() { return financingType; }
    public void setFinancingType(String financingType) { this.financingType = financingType; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }
    public String getPeriodValue() { return periodValue; }
    public void setPeriodValue(String periodValue) { this.periodValue = periodValue; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Double getMinCostRate() { return minCostRate; }
    public void setMinCostRate(Double minCostRate) { this.minCostRate = minCostRate; }
    public Double getMaxCostRate() { return maxCostRate; }
    public void setMaxCostRate(Double maxCostRate) { this.maxCostRate = maxCostRate; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
