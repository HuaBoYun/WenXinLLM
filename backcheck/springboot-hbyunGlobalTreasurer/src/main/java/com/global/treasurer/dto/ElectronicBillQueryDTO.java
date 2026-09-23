package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 电子票据查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "电子票据查询DTO", description = "电子票据查询数据传输对象")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectronicBillQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "电子票据号码")
    private String billNumber;

    @ApiModelProperty(value = "票据类型")
    private String billType;

    @ApiModelProperty(value = "票据状态")
    private String billStatus;

    @ApiModelProperty(value = "签名状态")
    private String signatureStatus;

    @ApiModelProperty(value = "验证状态")
    private String verificationStatus;

    @ApiModelProperty(value = "电子票据状态")
    private String electronicStatus;

    @ApiModelProperty(value = "出票人名称")
    private String drawerName;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "签发人名称")
    private String signerName;

    @ApiModelProperty(value = "出票日期开始")
    private String issueDateStart;

    @ApiModelProperty(value = "出票日期结束")
    private String issueDateEnd;

    @ApiModelProperty(value = "金额最小值")
    private java.math.BigDecimal amountMin;

    @ApiModelProperty(value = "金额最大值")
    private java.math.BigDecimal amountMax;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
    public String getSignatureStatus() { return signatureStatus; }
    public void setSignatureStatus(String signatureStatus) { this.signatureStatus = signatureStatus; }
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getElectronicStatus() { return electronicStatus; }
    public void setElectronicStatus(String electronicStatus) { this.electronicStatus = electronicStatus; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public String getSignerName() { return signerName; }
    public void setSignerName(String signerName) { this.signerName = signerName; }
    public String getIssueDateStart() { return issueDateStart; }
    public void setIssueDateStart(String issueDateStart) { this.issueDateStart = issueDateStart; }
    public String getIssueDateEnd() { return issueDateEnd; }
    public void setIssueDateEnd(String issueDateEnd) { this.issueDateEnd = issueDateEnd; }
    public java.math.BigDecimal getAmountMin() { return amountMin; }
    public void setAmountMin(java.math.BigDecimal amountMin) { this.amountMin = amountMin; }
    public java.math.BigDecimal getAmountMax() { return amountMax; }
    public void setAmountMax(java.math.BigDecimal amountMax) { this.amountMax = amountMax; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
