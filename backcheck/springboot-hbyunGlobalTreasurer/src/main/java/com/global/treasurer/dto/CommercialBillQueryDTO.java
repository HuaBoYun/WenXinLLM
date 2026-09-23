package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 商业汇票查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "商业汇票查询DTO", description = "商业汇票查询数据传输对象")
public class CommercialBillQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "汇票号码")
    private String billNo;

    @ApiModelProperty(value = "汇票类型")
    private String billType;

    @ApiModelProperty(value = "汇票状态")
    private String billStatus;

    @ApiModelProperty(value = "出票人名称")
    private String drawerName;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "承兑人名称")
    private String acceptorName;

    @ApiModelProperty(value = "出票日期开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDateStart;

    @ApiModelProperty(value = "出票日期结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDateEnd;

    @ApiModelProperty(value = "到期日期开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDateStart;

    @ApiModelProperty(value = "到期日期结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDateEnd;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public Date getIssueDateStart() { return issueDateStart; }
    public void setIssueDateStart(Date issueDateStart) { this.issueDateStart = issueDateStart; }
    public Date getIssueDateEnd() { return issueDateEnd; }
    public void setIssueDateEnd(Date issueDateEnd) { this.issueDateEnd = issueDateEnd; }
    public Date getMaturityDateStart() { return maturityDateStart; }
    public void setMaturityDateStart(Date maturityDateStart) { this.maturityDateStart = maturityDateStart; }
    public Date getMaturityDateEnd() { return maturityDateEnd; }
    public void setMaturityDateEnd(Date maturityDateEnd) { this.maturityDateEnd = maturityDateEnd; }

    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
