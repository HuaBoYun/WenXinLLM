package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 债券投资查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Data
@ApiModel(value = "BondInvestmentQueryDTO", description = "债券投资查询条件")
public class BondInvestmentQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("投资编号")
    private String investmentNo;

    @ApiModelProperty("债券代码")
    private String bondCode;

    @ApiModelProperty("债券类型")
    private String bondType;

    @ApiModelProperty("发行人")
    private String issuer;

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

    public String getBondCode() { return bondCode; }
    public void setBondCode(String bondCode) { this.bondCode = bondCode; }

    public String getBondType() { return bondType; }
    public void setBondType(String bondType) { this.bondType = bondType; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getInvestmentStatus() { return investmentStatus; }
    public void setInvestmentStatus(String investmentStatus) { this.investmentStatus = investmentStatus; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
