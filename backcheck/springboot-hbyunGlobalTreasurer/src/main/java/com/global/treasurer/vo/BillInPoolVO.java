package com.global.treasurer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据池内票据VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel("票据池内票据VO")
public class BillInPoolVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("明细ID")
    private Long detailId;

    @ApiModelProperty("票据池ID")
    private Long poolId;

    @ApiModelProperty("票据池名称")
    private String poolName;

    @ApiModelProperty("票据ID")
    private Long billId;

    @ApiModelProperty("票据号码")
    private String billNumber;

    @ApiModelProperty("票据类型")
    private String billType;

    @ApiModelProperty("票据金额")
    private BigDecimal billAmount;

    @ApiModelProperty("加入日期")
    private Date joinDate;

    @ApiModelProperty("退出日期")
    private Date exitDate;

    @ApiModelProperty("明细状态")
    private String detailStatus;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }
    public Date getExitDate() { return exitDate; }
    public void setExitDate(Date exitDate) { this.exitDate = exitDate; }
    public String getDetailStatus() { return detailStatus; }
    public void setDetailStatus(String detailStatus) { this.detailStatus = detailStatus; }
}
