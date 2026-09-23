package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 银行理财投资查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@ApiModel(value = "BankWealthInvestmentQueryDTO", description = "银行理财投资查询对象")
@Data
public class BankWealthInvestmentQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("投资编号")
    private String investmentNo;

    @ApiModelProperty("产品代码")
    private String productCode;

    @ApiModelProperty("银行代码")
    private String bankCode;

    @ApiModelProperty("投资状态")
    private String investmentStatus;

    @ApiModelProperty("开始日期")
    private Date startDate;

    @ApiModelProperty("结束日期")
    private Date endDate;

    // Getter和Setter
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public String getInvestmentNo() { return investmentNo; }
    public void setInvestmentNo(String investmentNo) { this.investmentNo = investmentNo; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getInvestmentStatus() { return investmentStatus; }
    public void setInvestmentStatus(String investmentStatus) { this.investmentStatus = investmentStatus; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
