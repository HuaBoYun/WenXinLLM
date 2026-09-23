package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据登记查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "票据登记查询DTO", description = "票据登记查询条件")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillRegistrationQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "票据号码(模糊查询)")
    private String billNumber;

    @ApiModelProperty(value = "票据类型")
    private String billType;

    @ApiModelProperty(value = "票据状态")
    private String billStatus;

    @ApiModelProperty(value = "出票人名称(模糊查询)")
    private String drawerName;

    @ApiModelProperty(value = "收款人名称(模糊查询)")
    private String payeeName;

    @ApiModelProperty(value = "承兑银行(模糊查询)")
    private String acceptingBank;

    @ApiModelProperty(value = "最小金额")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大金额")
    private BigDecimal maxAmount;

    @ApiModelProperty(value = "出票开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueStartDate;

    @ApiModelProperty(value = "出票结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueEndDate;

    @ApiModelProperty(value = "到期开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityStartDate;

    @ApiModelProperty(value = "到期结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityEndDate;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public String getAcceptingBank() { return acceptingBank; }
    public void setAcceptingBank(String acceptingBank) { this.acceptingBank = acceptingBank; }
    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    public Date getIssueStartDate() { return issueStartDate; }
    public void setIssueStartDate(Date issueStartDate) { this.issueStartDate = issueStartDate; }
    public Date getIssueEndDate() { return issueEndDate; }
    public void setIssueEndDate(Date issueEndDate) { this.issueEndDate = issueEndDate; }
    public Date getMaturityStartDate() { return maturityStartDate; }
    public void setMaturityStartDate(Date maturityStartDate) { this.maturityStartDate = maturityStartDate; }
    public Date getMaturityEndDate() { return maturityEndDate; }
    public void setMaturityEndDate(Date maturityEndDate) { this.maturityEndDate = maturityEndDate; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
