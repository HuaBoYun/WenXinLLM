package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资产品查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class InvestmentProductQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 产品代码 */
    private String productCode;

    /** 产品名称 */
    private String productName;

    /** 产品类型 */
    private String productType;

    /** 发行机构 */
    private String issuer;

    /** 风险等级 */
    private String riskLevel;

    /** 产品状态 */
    private String productStatus;

    /** 最小收益率 */
    private BigDecimal minReturnRate;

    /** 最大收益率 */
    private BigDecimal maxReturnRate;

    /** 发行日期开始 */
    private Date launchDateStart;

    /** 发行日期结束 */
    private Date launchDateEnd;

    /** 到期日期开始 */
    private Date maturityDateStart;

    /** 到期日期结束 */
    private Date maturityDateEnd;

    /** 币种 */
    private String currencyCode;

    /** 公司ID */
    private Long companyId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getProductStatus() { return productStatus; }
    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }
    public BigDecimal getMinReturnRate() { return minReturnRate; }
    public void setMinReturnRate(BigDecimal minReturnRate) { this.minReturnRate = minReturnRate; }
    public BigDecimal getMaxReturnRate() { return maxReturnRate; }
    public void setMaxReturnRate(BigDecimal maxReturnRate) { this.maxReturnRate = maxReturnRate; }
    public Date getLaunchDateStart() { return launchDateStart; }
    public void setLaunchDateStart(Date launchDateStart) { this.launchDateStart = launchDateStart; }
    public Date getLaunchDateEnd() { return launchDateEnd; }
    public void setLaunchDateEnd(Date launchDateEnd) { this.launchDateEnd = launchDateEnd; }
    public Date getMaturityDateStart() { return maturityDateStart; }
    public void setMaturityDateStart(Date maturityDateStart) { this.maturityDateStart = maturityDateStart; }
    public Date getMaturityDateEnd() { return maturityDateEnd; }
    public void setMaturityDateEnd(Date maturityDateEnd) { this.maturityDateEnd = maturityDateEnd; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
